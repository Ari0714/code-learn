package com.itbys.spark_basic.spark_core._03_transform_action

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object _07_reducebykey_aggregatebykey_foldbykey {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("_01_wc").setMaster("local[*]")
    val sc = new SparkContext(conf)

    val inputRDD: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6, 6, 10, 11), 1)
    val inputRDD02: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6, 7, 8, 9), 1)

    // TODO: 1.reducebykey


    // TODO: 2.aggregatebukey 。 初始值  (分区内计算规则,分区间计算规则)
    inputRDD
      .mapPartitions(x => x.map((_, 1)))
      .aggregateByKey(0)(_ + _, _ + _)
      .foreach(print(_))

    // TODO: 3.foldbykey . aggregatebukey中分区内和分区间计算规则一样
    inputRDD
      .mapPartitions(x => x.map((_, 1)))
      .foldByKey(0)(_ + _)
      .foreach(print(_))


  }


}
