package com.atguigu.flink_first_look.TableAPI_and_flinkSQL

import com.atguigu.flink_first_look.bean.SensorRead
import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api._
import org.apache.flink.table.api.scala._
import org.apache.flink.table.descriptors._
import org.apache.flink.types.Row

/**
  * Author xx
  * Date 2023/3/21
  * Desc
  */
object TimeFeature07 {

  def main(args: Array[String]): Unit = {

    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    val tableEnv: StreamTableEnvironment = StreamTableEnvironment.create(env)

    //datastream => table 定义
    val ds: DataStream[SensorRead] = env.readTextFile("04-flink-base/flink-base-scala-10/input/sensor.txt")
      .map { log =>
        val strings: Array[String] = log.split(",")
        SensorRead(strings(0), strings(1).toLong, strings(2).toDouble)
      }

    val tableTime: Table = tableEnv.fromDataStream(ds, 'num, 'timestamp, 'tmp, 'pt.proctime)


    //定义tableSchema时定义  仅仅kafka sink 时候用
    tableEnv.connect(new FileSystem().path("04-flink-base/flink-base-scala-10/input/sensor.txt"))
      .withFormat(new Csv())
      .withSchema(new Schema()
        .field("num", DataTypes.STRING())
        .field("timestamp", DataTypes.BIGINT())
        .field("tmp", DataTypes.DOUBLE())
        .field("pt", DataTypes.TIMESTAMP(3))
        .proctime()
      )
      .createTemporaryTable("inputTable")

    val tableTime2: Table = tableEnv.sqlQuery("select * from inputTable")

    tableTime2.printSchema()
    tableTime2.toAppendStream[Row].print()

    env.execute()

  }

}
