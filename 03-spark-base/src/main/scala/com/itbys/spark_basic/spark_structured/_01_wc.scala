package com.itbys.spark_basic.spark_structured

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.{StreamingQuery, Trigger}

/**
  * Author Ari
  * Date 2025/5/27
  * Desc structured streaming
  */
object _01_wc {

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

    val wordCounts = lines.flatMap(_.split("\\s+"))
      .groupBy("value")
      .count()

    val query: StreamingQuery = wordCounts.writeStream
      .outputMode("complete")
      .format("console")
      .trigger(Trigger.ProcessingTime(2000))
      .start()

    query.awaitTermination()
  }

}
