package com.itbys.spark_basic.spark_core._03_transform_action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object _09_join_leftouterjoin_cogroup {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val sc = new SparkContext(conf)

    val inputRDD: RDD[(String, Int)] = sc.makeRDD(List(("aa", 10), ("aa", 20), ("ff", 20), ("gg", 30)))
    val inputRDD02: RDD[(String, Int)] = sc.makeRDD(List(("aa", 10), ("bb", 20), ("cc", 20), ("dd", 30)))


    // TODO: 1.join
    val joinRDD: RDD[(String, (Int, Int))] = inputRDD.join(inputRDD02)

    // TODO: 1.leftouterjoin
    val leftjoinRDD: RDD[(String, (Int, Option[Int]))] = inputRDD.leftOuterJoin(inputRDD02)

    // TODO: 1.cogroup
    val cogroupRDD: RDD[(String, (Iterable[Int], Iterable[Int]))] = inputRDD.cogroup(inputRDD02)
  }


}
