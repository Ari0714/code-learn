package com.atguigu.flink_first_look.TableAPI_and_flinkSQL

import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.{DataTypes, Table}
import org.apache.flink.table.api.scala._
import org.apache.flink.table.descriptors.{Csv, FileSystem, Schema}

object TableSink03 {

  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val tableEnv: StreamTableEnvironment = StreamTableEnvironment.create(env)

    tableEnv.connect(new FileSystem().path("04-flink-base/flink-base-scala-10/input/sensor.txt"))
      .withFormat(new Csv())
      .withSchema(new Schema()
        .field("num", DataTypes.STRING())
        .field("timestamp", DataTypes.BIGINT())
        .field("tmp", DataTypes.DOUBLE())
      )
      .createTemporaryTable("inputTable")

    val allTable: Table = tableEnv.sqlQuery("select * from inputTable")

    val aggrTable: Table = tableEnv.sqlQuery("select num,count(*) from inputTable group by num")

    allTable.toAppendStream[(String, Long, Double)].print()

    aggrTable.toRetractStream[(String, Long)].print()


    //文件输出
    tableEnv.connect(new FileSystem().path("04-flink-base/flink-base-scala-10/input/sensor_output.txt"))
      .withFormat(new Csv())
      .withSchema(new Schema()
        .field("num", DataTypes.STRING())
        .field("timestamp", DataTypes.BIGINT())
        .field("tmp", DataTypes.DOUBLE())
      )
      .createTemporaryTable("outputTable")

    //追加流可以输出，aggr流无法输出（变化流）
    allTable.insertInto("outputTable")


    env.execute()

  }

}
