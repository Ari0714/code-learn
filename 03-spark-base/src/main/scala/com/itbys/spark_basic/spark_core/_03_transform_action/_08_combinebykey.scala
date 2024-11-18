package com.itbys.spark_basic.spark_core._03_transform_action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object _08_combinebykey {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val sc = new SparkContext(conf)

    // TODO: 1. combinebukey  可以让第一个数据结构转换
    sc.makeRDD(List(("aa", 10), ("aa", 20), ("bb", 20), ("bb", 30)))
      .combineByKey(
        (_, 1),
        (acc: (Int, Int), v) => (acc._1 + v, acc._2 + 1),
        (acc: (Int, Int), acc2: (Int, Int)) => (acc._1 + acc2._1, acc._2 + acc2._2)
      )
      .mapValues {
        case (x, y) =>
          x / y
      }
      .foreach(print(_))

  }
}
