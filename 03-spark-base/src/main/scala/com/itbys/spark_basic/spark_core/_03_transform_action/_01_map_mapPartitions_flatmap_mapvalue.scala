package com.itbys.spark_basic.spark_core._03_transform_action

import org.apache.spark.{SparkConf, SparkContext}

object _01_map_mapPartitions_flatmap_mapvalue {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val sc = new SparkContext(conf)

    // TODO: 1.map

    // TODO: 2.mapPartitions
    //选择：完成 > 快
    //对迭代器做map处理
    sc.textFile("03-spark-base/input/article.txt")
      .flatMap(_.split(" "))
      .mapPartitions(x => x.map((_, 2)))
      .reduceByKey(_ + _)
      .sortBy(_._2, false)
      .take(10)
      .foreach(println(_))

    // TODO: 3.flatmap


    // TODO: 4.mapvalue


  }


}
