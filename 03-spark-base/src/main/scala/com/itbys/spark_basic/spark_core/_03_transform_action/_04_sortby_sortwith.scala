package com.itbys.spark_basic.spark_core._03_transform_action

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object _04_sortby_sortwith {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val sc = new SparkContext(conf)

    val inputRDD: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6, 6), 1)

    //by为rdd的用法 ， with为scala用法
    // TODO: 1.by
    val byRDD: RDD[Int] = inputRDD.sortBy(x => x, false)
    //    byRDD.foreach(println(_))

    // TODO: 2.with
    byRDD.collect().sortWith((x, y) => x < y).foreach(println(_))


  }

}
