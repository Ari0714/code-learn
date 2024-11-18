package com.itbys.spark_basic.spark_core._04_accumutor_broadcast

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object _02_broadcast {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val sc = new SparkContext(conf)

    val inputRDD: RDD[Int] = sc.makeRDD(1 to 100)

    val broadcastRDD: Broadcast[RDD[Int]] = sc.broadcast(inputRDD)


  }


}
