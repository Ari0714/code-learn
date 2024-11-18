package com.itbys.spark_basic.spark_streaming._02_test

import com.itbys.spark_basic.util.{MyKafkaUtil, MyMySQLUtil}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Author  xx
  * Date  2023/12/28
  * Desc 计算评分数量
  */

object StreamingAnalysisWindow {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getName).setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(5))
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    import spark.implicits._

    var topic = "book_topic"
    val groupId = "group01"

    //读取kafka数据
    val valueDS: DStream[String] = MyKafkaUtil.getKafkaStream(ssc, topic, groupId).map(_.value())

    //获取平均评分最高的10本书
    val rkDS: DStream[(String, Double)] = valueDS.transform { rdd =>
      val modelRDD: RDD[MovieModel] = rdd.map { x =>
        val strings: Array[String] = x.split("\t")
        MovieModel(strings(0), strings(1), strings(2).toDouble)
      }

      val df: DataFrame = modelRDD.toDF()
      df.createOrReplaceTempView("book_topic")

      val resultDf: DataFrame = spark.sql(
        """
          |select rk, count(*) cnt
          |from book_topic
          |group by rk
          |order by cast(rk as double) desc
        """.stripMargin)

      resultDf.as[(String, Double)].rdd

    }

    //设置滑动窗口为60s，步长为10s
    val reduceDS: DStream[(String, Double)] = rkDS.reduceByKeyAndWindow((v1: Double, v2: Double) => v1 + v2, Seconds(60), Seconds(10))
    reduceDS.print()

    //保存数据库
    reduceDS.foreachRDD(rdd => MyMySQLUtil.saveToMysql(rdd.toDF("rk","cnt"),"book_rating"))

    ssc.start()
    ssc.awaitTermination()

  }


  case class MovieModel(uid: String, bid: String, rk: Double)


}
