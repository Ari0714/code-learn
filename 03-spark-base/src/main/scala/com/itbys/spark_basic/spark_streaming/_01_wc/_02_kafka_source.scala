package com.itbys.spark_basic.spark_streaming._01_wc

import com.itbys.spark_basic.bean.UserVisitAction
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.spark.SparkConf
import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql._
import org.apache.spark.streaming.dstream.{InputDStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}


object _02_kafka_source {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(5))

    // TODO: 1.基于receiver
    //        val kafkaDStream: ReceiverInputDStream[(String, String)] = KafkaUtils.createStream(ssc,
    //          "linux1:2181,linux2:2181,linux3:2181",
    //          "atguigu",
    //          Map[String, Int]("atguigu" -> 1))

    // TODO: 2.direct方式
    //3.定义Kafka参数
    val kafkaPara: Map[String, Object] = Map[String, Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "hdp101:9092,hdp102:9092,hdp103:9092",
      ConsumerConfig.GROUP_ID_CONFIG -> "atguigu",
      "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
      "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer"
    )

    val kafkaDS: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent, ConsumerStrategies.Subscribe[String, String](Set("aaa"), kafkaPara))


    ssc.start()
    ssc.awaitTermination()

  }
}
