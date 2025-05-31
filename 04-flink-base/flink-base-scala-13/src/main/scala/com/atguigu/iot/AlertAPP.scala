package com.atguigu.iot

import com.atguigu.iot.bean.{Alert, AlertRule, SensorReading}
import org.apache.flink.api.common.state.{MapStateDescriptor, ReadOnlyBroadcastState}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction
import org.apache.flink.util.Collector
import org.apache.flink.streaming.api.scala.OutputTag
import com.atguigu.iot.utils.MyKafkaUtil

/**
  * Author xx
  * Date 2025/5/27
  * Desc
  */
object AlertAPP {

  // 侧输出标签，带泛型
  val alertOutputTag: OutputTag[Alert] = new OutputTag[Alert]("alert-output") {}

  // 广播状态描述符
  val ruleDescriptor = new MapStateDescriptor[String, AlertRule]("rules", classOf[String], classOf[AlertRule])

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    // 数据流 (sensorId,timestamp,temp,humidity)
    val sensorStream: DataStream[SensorReading] = MyKafkaUtil.readFromKafka(env, "temp_data", "test-07")
      .filter(x => x.split(",").length == 4)
      .map { line =>
        val parts = line.split(",")
        SensorReading(parts(0), parts(1).toLong, parts(2).toDouble, parts(3).toDouble)
      }
    sensorStream.print()

    // 规则流 (sensorId,maxTemp,maxHumidity)
    //    val ruleStream: DataStream[AlertRule] = env.readTextFile("input/rule.txt")
    //      .map { line =>
    //        val parts = line.split(",")
    //        AlertRule(parts(0), parts(1).toDouble, parts(2).toDouble)
    //      }
    val ruleStream: DataStream[AlertRule] = env.fromCollection(List("sensor_1,30.0,20.0"))
      .map { line =>
        val parts = line.split(",")
        AlertRule(parts(0), parts(1).toDouble, parts(2).toDouble)
      }
    ruleStream.print()

    // 广播规则流
    val broadcastRules = ruleStream.broadcast(ruleDescriptor)

    // 连接流处理
    val processed: DataStream[SensorReading] = sensorStream
      .keyBy(_.sensorId)
      .connect(broadcastRules)
      .process(new AlertProcessFunction)

    // 正常输出
    processed.print()
    // 异常输出到侧流
    processed.getSideOutput(alertOutputTag).print()

    env.execute("IoT Monitoring Alert System")
  }

  class AlertProcessFunction extends KeyedBroadcastProcessFunction[String, SensorReading, AlertRule, SensorReading] {
    override def processElement(
                                 value: SensorReading,
                                 ctx: KeyedBroadcastProcessFunction[String, SensorReading, AlertRule, SensorReading]#ReadOnlyContext,
                                 out: Collector[SensorReading]
                               ): Unit = {
      val rules: ReadOnlyBroadcastState[String, AlertRule] = ctx.getBroadcastState(ruleDescriptor)
      val rule = rules.get(value.sensorId)

      if (rule != null && (value.temperature > rule.maxTemp || value.humidity > rule.maxHumidity)) {
        val msg = s"Exceeded limits: T=${value.temperature}/${rule.maxTemp}, H=${value.humidity}/${rule.maxHumidity}"
        val alert = Alert(value.sensorId, value.timestamp, value.temperature, value.humidity, msg)
        ctx.output(alertOutputTag, alert)
      } else {
        out.collect(value)
      }
    }

    override def processBroadcastElement(
                                          rule: AlertRule,
                                          ctx: KeyedBroadcastProcessFunction[String, SensorReading, AlertRule, SensorReading]#Context,
                                          out: Collector[SensorReading]
                                        ): Unit = {
      val state = ctx.getBroadcastState(ruleDescriptor)
      state.put(rule.sensorId, rule)
    }
  }

}
