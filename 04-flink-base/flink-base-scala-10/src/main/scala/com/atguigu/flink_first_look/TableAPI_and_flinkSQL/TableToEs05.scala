package com.atguigu.flink_first_look.TableAPI_and_flinkSQL

import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api._
import org.apache.flink.table.api.scala._
import org.apache.flink.table.descriptors._

/**
  * Author xx
  * Date 2023/3/21
  * Desc
  */
object TableToEs05 {

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


    //输出到Es  支持group by
    tableEnv.connect(new Elasticsearch()
      .version("6")
      .host("localhost", 9200, "http")
      .index("sensor")
      .documentType("temp"))
      .inUpsertMode()
      .withFormat(new Json())
      .withSchema(new Schema()
        .field("id", DataTypes.STRING())
        .field("count", DataTypes.BIGINT())
      )
      .createTemporaryTable("esOutputTable")


    allTable.insertInto("esOutputTable")


    env.execute()

  }

}
