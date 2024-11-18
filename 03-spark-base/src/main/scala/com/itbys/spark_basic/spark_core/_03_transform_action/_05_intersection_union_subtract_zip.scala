package com.itbys.spark_basic.spark_core._03_transform_action

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object _05_intersection_union_subtract_zip {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val sc = new SparkContext(conf)

    val inputRDD: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6, 6, 10, 11), 1)
    val inputRDD02: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5, 6, 7, 8, 9), 1)

    // TODO: 1.intersection
    val intersectionRDD: RDD[Int] = inputRDD.intersection(inputRDD02)
    intersectionRDD.foreach(println(_))

    // TODO: 2.union
    val unionRDD: RDD[Int] = inputRDD.union(inputRDD02)

    // TODO: 3.subtract
    val subtractRDD: RDD[Int] = inputRDD.subtract(inputRDD02)

    // TODO: 4.zip
    val zipRDD: RDD[(Int, Int)] = inputRDD.zip(inputRDD02)

    //交 并 差集 拉链
    //    intersectionRDD.foreach(println(_))
    //    unionRDD.foreach(println(_))
    //    subtractRDD.foreach(println(_))
    //    zipRDD.foreach(println(_))


  }

}
