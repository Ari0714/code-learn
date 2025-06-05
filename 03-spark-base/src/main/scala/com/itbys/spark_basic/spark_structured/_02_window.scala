package com.itbys.spark_basic.spark_structured

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.{StreamingQuery, Trigger}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.types._

/**
  * Author Ari
  * Date 2025/5/27
  * Desc structured streaming
  */
object _02_window {

  def main(args: Array[String]): Unit = {

    // 创建SparkSession
    val spark = SparkSession.builder().appName("StructuredStreamingWordCount").master("local[*]").getOrCreate()
    import spark.implicits._

    // 从socket源读取数据
    val lines = spark.readStream
      .format("socket")
      .option("host", "localhost")
      .option("port", 9999)
      .load()
      .as[String]

    // 开窗统计：窗口长度10分钟，滑动间隔5分钟
    val windowDuration = "10 minutes"
    val slideDuration = "5 minutes"

    val windowedCounts = lines
      .withWatermark("timestamp", "5 minutes") // 水位线延迟5分钟
      .groupBy(
      window($"timestamp", windowDuration, slideDuration),
      $"word"
    )
      .count()
      .orderBy("window") // 按窗口排序

    // 控制台输出
    val query = windowedCounts.writeStream
      .outputMode("update") // 使用update模式只输出有变化的行
      .format("console")
      .option("truncate", "false") // 不截断输出
      .trigger(Trigger.ProcessingTime("1 minute")) // 每分钟触发一次
      .start()

    println("Streaming started. Input data format: 'yyyy-MM-dd HH:mm:ss|word'")
    println(s"Window duration: $windowDuration, Slide duration: $slideDuration")


    query.awaitTermination()

  }

}
