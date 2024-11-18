package com.itbys.spark_basic.spark_streaming._01_wc

import com.itbys.spark_basic.bean.UserVisitAction
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object _01_wc {

  def main(args: Array[String]): Unit = {

    // TODO: 1.创建
    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(5))

    ssc.socketTextStream("hdp101", 7777)
      .map((_, 1))
      .reduceByKey(_ + _)
      .print()


    ssc.start()
    ssc.awaitTermination()

  }

}
