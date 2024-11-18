package com.itbys.spark_basic.spark_core._03_transform_action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object _06_groupby_partitionby_groupbykey {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("_01_wc").setMaster("local[*]")
    val sc = new SparkContext(conf)

    // TODO: 1.groupby
    sc.makeRDD(List("Hello", "hive", "hbase", "Hadoop"))
      .groupBy(_.charAt(0))
    //      .foreach(println(_))


    // TODO: 2.groupbykey
    sc.makeRDD(List((1, 1), (1, 2), (2, 1)))
      .groupByKey()
      .foreach(println(_))


    //    k 1 , v iter(1,2)

    // TODO: 3.partitionby


  }

}
