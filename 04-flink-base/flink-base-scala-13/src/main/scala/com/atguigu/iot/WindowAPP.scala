package com.atguigu.iot

import com.atguigu.iot.bean.SensorReading
import org.apache.flink.api.common.functions.{AggregateFunction, ReduceFunction}
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala.{DataStream, OutputTag, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.WindowFunction
import com.atguigu.iot.utils.MyKafkaUtil

/**
  * Author xx
  * Date 2025/5/27
  * Desc 
  */
object WindowAPP {

  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val readKafkaData: DataStream[String] = MyKafkaUtil.readFromKafka(env, "temp_data", "test-window-14")
    readKafkaData.print()

    val SensorData: DataStream[SensorReading] = readKafkaData
      .filter(x => x.split(",").length == 4)
      .map { x =>
        val strings: Array[String] = x.split(",")
        SensorReading(strings(0), strings(1).toLong, strings(2).toDouble, strings(3).toDouble)
      }.assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[SensorReading](Time.seconds(3)) {
      override def extractTimestamp(t: SensorReading): Long = t.timestamp * 1000
    })
    SensorData.print("xxxx")

    //窗口计算平均温度，湿度
    val SensorDataRes: DataStream[(String, Double, Double)] = SensorData
      .keyBy(_.sensorId)
      .timeWindow(Time.seconds(2))
      .aggregate(new AvgTempHumAggregate)
    SensorDataRes.print()


    env.execute()

  }

  class AvgTempHumAggregate extends AggregateFunction[SensorReading, (Double, Double, Long), (String, Double, Double)] {
    var currentSensorId: String = _

    override def createAccumulator(): (Double, Double, Long) = (0.0, 0.0, 0L)

    override def add(value: SensorReading, acc: (Double, Double, Long)): (Double, Double, Long) = {
      currentSensorId = value.sensorId
      (acc._1 + value.temperature, acc._2 + value.humidity, acc._3 + 1)
    }

    override def getResult(acc: (Double, Double, Long)): (String, Double, Double) = {
      val avgTemp = if (acc._3 > 0) acc._1 / acc._3 else 0.0
      val avgHum = if (acc._3 > 0) acc._2 / acc._3 else 0.0
      (currentSensorId, avgTemp, avgHum)
    }

    override def merge(a: (Double, Double, Long), b: (Double, Double, Long)): (Double, Double, Long) =
      (a._1 + b._1, a._2 + b._2, a._3 + b._3)
  }


}
