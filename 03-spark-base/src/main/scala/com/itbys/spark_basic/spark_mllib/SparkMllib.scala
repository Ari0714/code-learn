package com.itbys.spark_basic.spark_mllib

import org.apache.spark.SparkConf
import org.apache.spark.ml.classification.{DecisionTreeClassificationModel, DecisionTreeClassifier}
import org.apache.spark.mllib.linalg
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.{LabeledPoint, LinearRegressionModel, LinearRegressionWithSGD}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql._
import org.apache.spark.mllib.classification.{LogisticRegressionModel, LogisticRegressionWithLBFGS, SVMWithSGD}
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.mllib.fpm.FPGrowth
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.tree.DecisionTree

/**
  * Author xx
  * Date 2023/7/15
  * Desc 各种算法
  */
object SparkMllib {

  def main(args: Array[String]): Unit = {

    testKmeans()

  }


  /**
    * @desc k-means
    */
  def testKmeans() = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getName).setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    import spark.implicits._

    val inputDF: Dataset[Row] = spark.read
      .format("csv")
      .option("header", "true")
      .load("input/爱奇艺视频数据.csv")
    inputDF.show()

    //获取release_date列并转化为RDD
    val viewsRDD: RDD[Double] = inputDF.select("number_of_views")
      .where("number_of_views != null or length(number_of_views) != 0").as[Double].rdd

    //提取特征，建立模型
    val parsedData: RDD[linalg.Vector] = viewsRDD
      .map { x => Vectors.dense(x) }

    //聚类中心个数，算法迭代次数，算法运行次数
    val numClusters = 1
    val numIterations = 20
    val runs = 10

    //KMeans训练
    val kmeansModel = KMeans.train(parsedData, numClusters, numIterations, runs)

    //打印聚类中心ID
    kmeansModel.clusterCenters.foreach(x => println("影视播放量的聚类中心: " + x.apply(0).toInt))

  }


  /**
    * @desc LinearRegressionWithSGD
    */
  def readFromCsv() = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getName).setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    import spark.implicits._

    //input：
    val inputDF: DataFrame = spark.read
      .option("header", "true")
      .csv("output/01/part-00000-499a0a6d-dec0-4ce0-96d7-81a61cf416a4-c000.csv")
    //        inputDF.show()
    inputDF.createOrReplaceTempView("air_info")


    /**
      * 线性回归分析
      * 特征：month, day_of_week, distance, departure_time 。标签：delay
      */
    //1、delay标签值需要计算
    val resDF: DataFrame = spark.sql(
      """
        |select month, day_of_week, distance, departure_time, arrival_delay
        |from air_info
      """.stripMargin)
    resDF.show()
    val resRDD: RDD[(String, String, String, String, String)] = resDF.as[(String, String, String, String, String)].rdd

    //特征提取
    val parseData: RDD[LabeledPoint] = resRDD
      .map { x =>
        LabeledPoint(x._5.toDouble, Vectors.dense(x._1.toDouble, x._2.toDouble, x._3.toDouble, x._4.toDouble))
      }

    //样本数据划分,训练样本占0.8,测试样本占0.2
    val dataParts = parseData.randomSplit(Array(0.9, 0.1))
    val trainRDD = dataParts(0)
    val testRDD = dataParts(1)

    //建立LogisticRegression模型并训练，模型训练
    val model: LinearRegressionModel = LinearRegressionWithSGD.train(trainRDD, 5, 0.000000560)

    //测试集预测
    val predictionAndLabels: RDD[(linalg.Vector, Double, Double)] = testRDD.map {
      case LabeledPoint(label, features) =>
        val prediction = model.predict(features)
        (features, prediction.toInt, label)
    }
    predictionAndLabels.foreach(println(_))


    //模型评估, 计算模型预测的平均差
    val predictDF: DataFrame = predictionAndLabels.toDF("features", "predict", "label")
    predictDF.createOrReplaceTempView("predict_result")
    spark.sql(
      """
        |select sum(sub) / cnt predict_sub from (
        | select (predict - label) sub, count(*) over() cnt from predict_result
        |) t group by cnt
      """.stripMargin)
      .show()

  }


  /**
    * @desc LogisticRegressionWithLBFGS（0，1）
    */
  def readFromXlsx() = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getName).setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    import spark.implicits._

    //input: 5067,987263777,1708934283,664221295,356.04,271.56,20170501,0,26,20090730,C
    //1代表正常 、0代表异常
    //选取特征为flow、flow_last_one、month_fee、months_3avg、phone_change、age
    val mapTrainRDD: RDD[(String, String, String, String, String, String, Int)] = spark.sparkContext.textFile("input/训练集.csv")
      .filter(_.length > 10)
      .filter(!_.contains("USER_ID"))
      .map {
        x =>
          val strings: Array[String] = x.split(",")
          var status = 0
          if (strings(10) == "A") status = 1 else 0
          (strings(1), strings(2), strings(4), strings(5), strings(7), strings(8), status)
      }

    //特征提取
    val parseDate: RDD[LabeledPoint] = mapTrainRDD
      .map { x =>
        LabeledPoint(x._7.toDouble, Vectors.dense(x._1.toDouble, x._2.toDouble, x._3.toDouble,
          x._4.toDouble, x._5.toDouble, x._6.toDouble))
      }

    //数据划分
    val splits: Array[RDD[LabeledPoint]] = parseDate.randomSplit(Array(0.6, 0.4), seed = 11L)
    val training = splits(0).cache()
    val test = splits(1)

    //模型训练
    val model: LogisticRegressionModel = new LogisticRegressionWithLBFGS().setNumClasses(3).run(training)
    //    println(model)

    //测试数据预测
    val predictionAndLabels: RDD[(Double, Double)] = test.map { case LabeledPoint(label, features) =>
      val prediction = model.predict(features)
      (prediction, label)
    }
    //    predictionAndLabels.foreach(println(_))


    //计算准确率
    val metrics = new MulticlassMetrics(predictionAndLabels.map(x => (x._1.toDouble, x._2.toDouble)))
    val precision = metrics.precision
    println("Precision = " + precision)

  }


  /**
    * @desc 频繁项集，FPGrowth
    */
  def testFPGrowth(rdd: RDD[Array[String]]) = {

    /**
      * 建立模型
      */
    // 设置最小支持度
    val minSupport = 0
    // 设置并行分区数
    val numPartition = 10
    val model = new FPGrowth().setMinSupport(minSupport).setNumPartitions(numPartition).run(rdd)

    // 频繁项集的个数
    println(s"频繁项集的个数：${model.freqItemsets.count()}")

    //查看频繁项集
    val groups = model.freqItemsets.filter(_.items.length == 2)
    groups.foreach(println(_))

  }


  /**
    * @desc SVM
    */
  def testSVM(rdd: RDD[Array[String]]) = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder.config(conf).getOrCreate()

    //id,item,category,behavior,date,hour
    //165976,4606473,4170419,pv,2017-11-26,21
    // 加载和解析数据文件
    val data = spark.sparkContext.textFile("input/UserBehavior.csv")
      .filter(!_.contains("id"))


    val parsedData = data.map {
      line =>
        val strings = line.split(",")
        var pv: Int = 0
        if (strings(3) == "pv") pv = 1
        LabeledPoint(pv.toDouble, Vectors.dense(strings(1).toDouble, strings(2).toDouble))
    }

    //数据划分
    val splits: Array[RDD[LabeledPoint]] = parsedData.randomSplit(Array(0.6, 0.4), seed = 11L)
    val training = splits(0).cache()
    val test = splits(1)

    // 设置迭代次数并进行进行训练
    val numIterations = 10
    val model = SVMWithSGD.train(training, numIterations)

    // 统计分类错误的样本比例
    val labelAndPreds = test.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }

    val trainErr = labelAndPreds.filter(r => r._1 != r._2).count.toDouble / parsedData.count
    println("Training Error = " + trainErr)

    val metrics = new MulticlassMetrics(labelAndPreds)
    val precision = metrics.precision
    println("Precision = " + precision)

  }


  /**
    * @desc DecisionTree
    */
  def testDecisionTree(rdd: RDD[Array[String]]) = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder.config(conf).getOrCreate()

    //id,item,category,behavior,date,hour
    //165976,4606473,4170419,pv,2017-11-26,21
    // 加载和解析数据文件
    val data = spark.sparkContext.textFile("input/UserBehavior.csv")
      .filter(!_.contains("id"))

    val parsedData = data.map { line =>
      val strings = line.split(",")
      var pv: Int = 0
      if (strings(3) == "pv") pv = 1
      LabeledPoint(pv.toDouble, Vectors.dense(strings(1).toDouble, strings(2).toDouble))
    }

    //数据划分
    val splits: Array[RDD[LabeledPoint]] = parsedData.randomSplit(Array(0.6, 0.4), seed = 11L)
    val training = splits(0).cache()
    val test = splits(1)

    // 设置迭代次数等参数并进行进行训练
    val numClasses = 3
    //分类数
    val maxDepth: Int = 5 //树的最大深度
    val maxBins = 30
    //离散连续特征时使用的bin数。增加maxBins允许算法考虑更多的分割候选者并进行细粒度的分割决策。
    val impurity = "gini"
    val categoricalFeaturesInfo = Map[Int, Int]() //空的categoricalFeaturesInfo表示所有功能都是连续的。
    val model = DecisionTree.trainClassifier(training, numClasses, categoricalFeaturesInfo, impurity, maxDepth, maxBins)

    //测试集数据预测
    val labelAndPreds = test.map { point =>
      val prediction = model.predict(point.features)
      (point.label, prediction)
    }
    labelAndPreds.foreach(println(_))

    //预测准确度
    val metrics = new MulticlassMetrics(labelAndPreds)
    val precision = metrics.precision
    println("Precision = " + precision)

  }

}
