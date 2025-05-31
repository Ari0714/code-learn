package com.atguigu.iot.utils

import java.util.Properties
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaProducer}

/**
  * Author xx
  * Date 2025/5/27
  * Desc kafka 工具类
  */
object MyKafkaUtil {

  def getKafkaConsumer(topic: String, groupId: String): FlinkKafkaConsumer[String] = {
    val props = new Properties()
    props.setProperty("bootstrap.servers", "hdp:9092")
    props.setProperty("group.id", groupId)
    props.setProperty("auto.offset.reset", "earliest")

    new FlinkKafkaConsumer[String](topic, new SimpleStringSchema(), props)
  }


  def getKafkaProducer(topic: String): FlinkKafkaProducer[String] = {
    new FlinkKafkaProducer[String](
      "hdp:9092",
      topic,
      new SimpleStringSchema()
    )
  }

  def readFromKafka(env: StreamExecutionEnvironment, topic: String, groupId: String): DataStream[String] = {
    env.addSource(getKafkaConsumer(topic, groupId))
  }

  def writeToKafka(stream: DataStream[String], topic: String): Unit = {
    stream.addSink(getKafkaProducer(topic))
  }


  // send data
  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

//    val readFile = env.readTextFile("input/sensor.txt")
//    readFile.print()
//    writeToKafka(readFile,"temp_data")


    val readFile = env.readTextFile("input/sensor-sql.txt")
    readFile.print()
    writeToKafka(readFile,"temp_data_sql")

//    val readKafkaData: DataStream[String] = readFromKafka(env,"temp_data","test-01")
//    readKafkaData.print()


    env.execute()

  }


}
