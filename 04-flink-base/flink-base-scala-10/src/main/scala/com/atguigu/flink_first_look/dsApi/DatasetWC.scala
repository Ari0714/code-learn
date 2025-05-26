package com.atguigu.flink_first_look.dsApi

import org.apache.flink.api.java.DataSet
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.api.scala
import org.apache.flink.api.scala.{AggregateDataSet, ExecutionEnvironment}
import org.apache.flink.streaming.api.scala._

/**
  * Author Ari
  * Date 2023/3/21
  * Desc 
  */
object DatasetWC {

  def main(args: Array[String]): Unit = {

    //    val tool: ParameterTool = ParameterTool.fromArgs(args)
    //    val input: String = tool.get("input")
    //    val output: String = tool.get("output")

    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment

    val resDS = env.readTextFile("04-flink-base/flink-base-java-13/input/article.txt")
      .flatMap(_.split(" "))
      .map((_, 1))
      .groupBy(0)
      .sum(1)
    resDS.print()

    //    resDS.writeAsCsv("output/01").setParallelism(1)


    //    environment.execute()

  }
}
