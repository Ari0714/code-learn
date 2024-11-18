package com.itbys.spark_basic.spark_core._03_transform_action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object _02_glom_filter_distinct_sample {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val sc = new SparkContext(conf)

    val inputRDD: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6, 6))
    val inputRDD02: RDD[List[Int]] = sc.makeRDD(List(List(1, 2, 3), List(4, 5, 6, 6)))

    // TODO: 1.分区转换内存数组，分区不变
    val glomRDD: RDD[Array[Int]] = inputRDD.glom()

    // TODO: 2.filter
    inputRDD.filter(x => x > 2)


    // TODO: 3.distinct
    inputRDD.distinct().foreach(println(_))

    // TODO: 4.sample
    inputRDD.sample(false, 0.5).foreach(println(_))


  }

}
