package com.atguigu.iot

import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api._
import org.apache.flink.table.api.bridge.scala._
import org.apache.flink.types.Row

/**
  * Author xx
  * Date 2025/5/27
  * Desc
  */
object TableSqlAPP {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val tableEnv = StreamTableEnvironment.create(env)

    // 注册 Kafka Source 表
    tableEnv.executeSql(
      """
        |CREATE TABLE sensor_data (
        |  sensorId STRING,
        |  `timestamp` BIGINT,
        |  temperature DOUBLE,
        |  humidity DOUBLE,
        |  `ts` AS TO_TIMESTAMP_LTZ(`timestamp`, 3),
        |  WATERMARK FOR ts AS ts - INTERVAL '5' SECOND
        |) WITH (
        |  'connector' = 'kafka',
        |  'topic' = 'temp_data_sql',
        |  'properties.bootstrap.servers' = 'hdp:9092',
        |  'properties.group.id' = 'groupsql-3',
        |  'scan.startup.mode' = 'earliest-offset',
        |  'format' = 'json'
        |)
        |""".stripMargin)
    val sensor_table: Table = tableEnv.sqlQuery("select * from sensor_data")
    sensor_table.toAppendStream[Row].print("sensor_table")

    // 注册 Kafka Sink 表
    tableEnv.executeSql(
      """
        |CREATE TABLE sensor_stats (
        |  sensorId STRING,
        |  window_start TIMESTAMP(3),
        |  window_end TIMESTAMP(3),
        |  avgTemp DOUBLE,
        |  avgHumidity DOUBLE
        |) WITH (
        |  'connector' = 'kafka',
        |  'topic' = 'temp_data_output',
        |  'properties.bootstrap.servers' = 'hdp:9092',
        |  'format' = 'json'
        |)
        |""".stripMargin)

    // 编写窗口 SQL：按 sensorId 滚动 5 秒统计平均温度湿度
    val resultTable = tableEnv.sqlQuery(
      """
        |SELECT
        |  sensorId,
        |  TUMBLE_START(ts, INTERVAL '5' SECOND) AS window_start,
        |  TUMBLE_END(ts, INTERVAL '5' SECOND) AS window_end,
        |  AVG(temperature) AS avgTemp,
        |  AVG(humidity) AS avgHumidity
        |FROM sensor_data
        |GROUP BY
        |  TUMBLE(ts, INTERVAL '5' SECOND),
        |  sensorId
        |""".stripMargin)
    resultTable.toAppendStream[Row].print("resultTable")

    // 将结果插入 Kafka Sink 表
    resultTable.executeInsert("sensor_stats")


    env.execute()
  }
}
