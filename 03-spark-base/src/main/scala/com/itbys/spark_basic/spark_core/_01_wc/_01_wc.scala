//package com.itbys.spark_basic.spark_core._01_wc
//
//import com.huaban.analysis.jieba.JiebaSegmenter
//import org.apache.spark.rdd.RDD
//import org.apache.spark.sql.SparkSession
//import org.apache.spark.{SparkConf, SparkContext}
//
//object _01_wc {
//
//  def main(args: Array[String]): Unit = {
//
//    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
//    val sc = new SparkContext(conf)
//
//    val resRDD: RDD[(String, Int)] = sc.textFile("03-spark-base/input/article.txt")
//      .flatMap(_.split(" "))
//      .map((_, 1))
//      .reduceByKey(_ + _)
//      .repartition(1)
//      .sortBy(_._2, false)
//    resRDD.foreach(println(_))
//
////    resRDD.repartition(1).saveAsTextFile("03-spark-base/output/01")
//
//  }
//
//
//  /**
//    * @desc 使用jieba切词的wordcounf
//    */
//  def testJiebaWd(): Unit = {
//
//    val conf: SparkConf = new SparkConf().setAppName(getClass.getName).setMaster("local[*]")
//    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
//    import spark.implicits._
//
//    //读取数据
//    val inputRDD: RDD[String] = spark.sparkContext.textFile("input/")
//
//    //使用jieba切词
//    val jiebaRDD: RDD[String] = inputRDD.flatMap { x =>
//      val segmenter = new JiebaSegmenter
//      segmenter.sentenceProcess(x).toArray().map(_.toString)
//    }
//
//    //过滤符号、语气词等、统计词频
//    val resultRDD: RDD[(String, Int)] = jiebaRDD
//      .filter(x => x.trim.length > 1 && !x.contains("“") && !x.contains("”"))
//      .map((_, 1))
//      .reduceByKey(_ + _)
//      .sortBy(_._2, false)
//      .repartition(1)
//
//    //输出前20
//    resultRDD.take(20).foreach(println(_))
//
//
//}
