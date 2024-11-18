package com.itbys.spark_basic.spark_core._03_transform_action

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object _11_collect_take_first {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val sc = new SparkContext(conf)

    val inputRDD: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6, 6, 10, 11), 1)

    // TODO: first collect take
    println(inputRDD.first())


  }

}
