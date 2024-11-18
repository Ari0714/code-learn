package com.itbys.spark_basic.spark_streaming._02_test

import com.itbys.spark_basic.bean.AdsLog
import com.itbys.spark_basic.util.MyKafkaUtil
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.DStream

object _02_ad_click_count {

  def main(args: Array[String]): Unit = {

    val topic = "spark_stream_test"

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(5))

    // TODO: 1.获取kafka数据
    val kafkaDS: DStream[String] = MyKafkaUtil.getKafkaStream(ssc, topic).map(_.value())

    val adslogDS: DStream[AdsLog] = kafkaDS.map { x =>
      val strings: Array[String] = x.split(" ")
      AdsLog(strings(0).toLong, strings(1), strings(2), strings(3), strings(4))
    }


  }

}
