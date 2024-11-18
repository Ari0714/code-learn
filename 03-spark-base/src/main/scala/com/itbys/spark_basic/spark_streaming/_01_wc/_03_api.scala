package com.itbys.spark_basic.spark_streaming._01_wc

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object _03_api {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(5))
    ssc.checkpoint("./ck")

    // TODO: 1.无状态
    // map flatmap filter reduceByKey groupByKey
    // transform join

    // TODO: 2.有状态
    //updateStateByKey
    ssc.socketTextStream("hdp101", 7777)
      .map((_, 1L))
      .updateStateByKey { (values: Seq[Long], old: Option[Long]) =>
        var cnt = 0L
        if (old.isDefined)
          cnt = old.get
        for (elem <- values) {
          cnt += elem
        }
        Option(cnt)
      }
      .print()

    //windowOperation: reduceByKeyAndWindow  窗口 步长
    ssc.socketTextStream("hdp101", 7777)
      .map((_, 1))
      .reduceByKeyAndWindow((a: Int, b: Int) => a + b, Seconds(20), Seconds(10))
      .print()

    ssc.start()
    ssc.awaitTermination()
  }


  // 定义更新状态方法，参数values为当前批次单词频度，state为以往批次单词频度
  val updateFunc = (values: Seq[Int], state: Option[Int]) => {
    val currentCount = values.foldLeft(0)(_ + _)
    val previousCount = state.getOrElse(0)
    Some(currentCount + previousCount)
  }


}
