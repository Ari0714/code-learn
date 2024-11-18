package com.itbys.spark_basic.spark_core._02_source

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object _01_source {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val sc = new SparkContext(conf)

    // TODO: 1.集合
    //Seq  List
    //makeRDD 本质是 parallelize
    val inputRDD: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4), 4)
    sc.parallelize(List(1, 2, 3, 4, 5), 5)

    // TODO: 2.本地文件
    sc.textFile("input/article.txt")

    // TODO: 3.hdfs等
    sc.textFile("hdfs://hdp101:9000/test/article.txt").foreach(println(_))

    // TODO: 4.继承 从其他RDD创建
    val filterRDD: RDD[Int] = inputRDD.filter(_ > 2)


  }

}
