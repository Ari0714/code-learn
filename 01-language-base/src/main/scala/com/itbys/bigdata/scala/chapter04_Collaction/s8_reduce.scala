package com.itbys.bigdata.scala.chapter04_Collaction

import scala.collection.mutable

/**
  * Author xx
  * Date 2023/3/14
  * Desc
  */
object s8_reduce {

  def main(args: Array[String]): Unit = {

    val ints = List(6, 7, 8, 9)

    //化简：加减乘除
    println(ints.reduce(_ * _))
    println(ints.reduceLeft(_ + _))
    println(ints.reduceRight(_ - _))


    //fold reduce 外传参数
    println(ints.fold(100)(_ + _))
    println(ints.fold(100)(_ - _))

    //foldmap
    val stringToInt1 = mutable.Map("a" -> 1, "b" -> 2, "c" -> 3)
    val stringToInt2 = mutable.Map("d" -> 1, "e" -> 2, "a" -> 3)

    val stringToInt = stringToInt1.foldLeft(stringToInt2)((map, t) => {

      map(t._1) = map.getOrElse(t._1, 0) + t._2
      map

    })

    println(stringToInt)

  }

}
