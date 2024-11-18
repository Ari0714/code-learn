package com.itbys.bigdata.scala.chapter04_Collaction

import scala.collection.mutable

/**
  * Author xx
  * Date 2023/3/14
  * Desc
  */
object s3_set {

  def main(args: Array[String]): Unit = {

    //无序，去重
    val set1 = Set(1, 2, 3, 4, 5, 1)
    val set2 = Set(5, 6, 7, 8, 9, 9)
    println(set1.mkString(","))

    println(set1 + 99)
    println(set1 - 1)


    println(set1 ++ set2)


    //可变
    val mset1 = mutable.Set(1, 2, 3, 4)

  }

}
