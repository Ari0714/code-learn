package com.itbys.bigdata.scala.chapter02_function

/**
  * Author xx
  * Date 2023/3/14
  * Desc
  */
object _14_Exception {

  def main(args: Array[String]): Unit = {

    val test1 = new test15

  }

}

trait test14 extends Exception {

  println("sss")
  println(this.getMessage)

}

trait test14_1 {

  //指定使用范围，前面不能有东西
  this: Exception =>
  def test01: Unit = {
    this.getMessage
  }

}

class test15 extends Exception with test14_1 {


}

