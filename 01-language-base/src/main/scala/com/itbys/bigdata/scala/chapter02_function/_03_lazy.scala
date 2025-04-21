package com.itbys.bigdata.scala.chapter02_function

/**
  * Author xx
  * Date 2023/3/14
  * Desc
  */
object _03_lazy {

  def main(args: Array[String]): Unit = {
    lazy val res = sum(10, 20)
    println("-----------------")
    println("res=" + res)
  }


  def sum(n1: Int, n2: Int): Int = {
    println("sum() 执行了..")
    n1 + n2
  }


}
