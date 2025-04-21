package com.itbys.bigdata.scala.chapter03_implicit

/**
  * Author xx
  * Date 2023/3/14
  * Desc
  */
object _01_test {

  def main(args: Array[String]): Unit = {

    //隐式转换  implicit修饰
    implicit def DouToInt(d: Double) = d.toInt

    //    val i = DouToInt(5.0)

    val a = 4.0

    println(a)


  }


}
