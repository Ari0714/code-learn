package com.itbys.spark_basic.spark_core._04_accumutor_broadcast


import org.apache.spark.util.{AccumulatorV2, LongAccumulator}
import org.apache.spark.{SparkConf, SparkContext}
import scala.collection._


object _01_accumutor {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("_01_wc").setMaster("local[*]")
    val sc = new SparkContext(conf)

    // TODO: 1.自带longaccumutor
    val a: LongAccumulator = sc.longAccumulator("a")

    for (i <- 1 to 100) {
      a.add(i)
    }

    //    println(a.value)

    // TODO: 2.自定义累加器
    val accumulatorV = new MyAccumulatorV2
    sc.register(accumulatorV)

    //使用要有action算子触发
    sc.makeRDD(List("aa", "bb", "cc"))
      .foreach(x => accumulatorV.add(x))

    println(accumulatorV.value)

  }

}


//自定义累加器
//class wordAccumulator extends AccumulatorV2[String,com.tcl.com.itbys.util.ArrayList[String]]{
//
//  val list: com.tcl.com.itbys.util.ArrayList[String] = new com.tcl.com.itbys.util.ArrayList[String]
//
//  override def isZero: Boolean = list.isEmpty
//
//  override def copy(): AccumulatorV2[String, com.tcl.com.itbys.util.ArrayList[String]] = new wordAccumulator
//
//  override def reset(): Unit = list.clear()
//
//  override def add(v: String): Unit = {
//    if(v.contains("h"))
//      list.add(v)
//  }
//
//  override def merge(com.itbys.other: AccumulatorV2[String, com.tcl.com.itbys.util.ArrayList[String]]): Unit = {
//
//    list.addAll(com.itbys.other.value)
//
//  }
//
//  override def value: com.tcl.com.itbys.util.ArrayList[String] = list
//}


class MyAccumulatorV2 extends AccumulatorV2[String, mutable.Map[String, Long]] {

  var map = mutable.Map[String, Long]()

  override def isZero: Boolean = map.isEmpty

  override def copy(): AccumulatorV2[String, mutable.Map[String, Long]] = new MyAccumulatorV2

  override def reset(): Unit = map.clear()

  override def add(v: String): Unit = {
    map(v) = map.getOrElse(v, 0L) + 1L
  }

  override def merge(other: AccumulatorV2[String, mutable.Map[String, Long]]): Unit = {
    map = map.foldLeft(other.value)(
      (innerMap, kv) => {
        innerMap(kv._1) = innerMap.getOrElse(kv._1, 0L) + kv._2
        innerMap
      }
    )
  }

  override def value: mutable.Map[String, Long] = map

}


//class MyAccumulatorV2 extends AccumulatorV2[Int,Int] {
//
//  var cur:Int = 0
//
//  override def isZero: Boolean = cur == 0
//
//  override def copy(): AccumulatorV2[Int, Int] = new MyAccumulatorV2
//
//  override def reset(): Unit = cur = 0
//
//  override def add(v: Int): Unit = cur + v
//
//  override def merge(com.itbys.other: AccumulatorV2[Int, Int]): Unit = com.itbys.other.value + cur
//
//  override def value: Int = cur
//
//}
