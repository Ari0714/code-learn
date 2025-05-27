package com.itbys.spark_basic.spark_structured

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Author Ari
  * Date 2025/5/27
  * Desc 配置类
  */
object _01_wc {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(5))

    ssc.socketTextStream("hdp101", 7777)
      .map((_, 1))
      .reduceByKey(_ + _)
      .print()


    ssc.start()
    ssc.awaitTermination()

  }

}
