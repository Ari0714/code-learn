package com.itbys.spark_basic.spark_core._03_transform_action

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object _03_coalesce_repartition {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val sc = new SparkContext(conf)

    val inputRDD: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6, 6))


    //coalesce 只是缩减分区 shuffle默认为fales 不打乱数据  分区数据还是不均匀 ， repartition默认true解决问题
    // TODO: 1.coalesce
    val coalesceRDD: RDD[Int] = inputRDD.coalesce(2, false)

    // TODO: 2.repartition
    val repartitionRDD: RDD[Int] = inputRDD.repartition(2)


  }


}
