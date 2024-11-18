package com.itbys.spark_basic.spark_core._03_transform_action

import org.apache.spark.{SparkConf, SparkContext}


object _10_test {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val sc = new SparkContext(conf)

    //1516609143869 8 2 91 27  时间戳，省份，城市，用户，广告
    //统计出每一个省份每个广告被点击数量排行的Top3
    //每个省份 =》 分组操作
    sc.textFile("input/agent.log")
      .map { x =>
        val strings: Array[String] = x.split(" ")
        ((strings(1), strings(4)), 1)
      }
      .reduceByKey(_ + _)
      .map(a => (a._1._1, (a._1._2, a._2)))
      .groupByKey()
      .mapValues { iter =>
        iter.toList.sortWith((x, y) => x._2 > y._2).take(3)
      }
      .collect()
      .foreach(print(_))

  }


}
