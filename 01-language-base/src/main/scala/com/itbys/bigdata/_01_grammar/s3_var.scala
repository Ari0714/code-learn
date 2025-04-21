package com.itbys.bigdata._01_grammar

import scala.io.StdIn

/**
  * Author xx
  * Date 2023/3/14
  * Desc
  */
object s3_var {

  def main(args: Array[String]): Unit = {

    val name = "xiaoming"
    val age = 8
    val salay = 3500
    val sex = 'Y'

    println("name=" + name + ",age=" + age + ",salay=" + salay + ",sex=" + sex)

    //    val ss : String= "hello"
    //    val d : Boolean = true
    //    val ll = 10000000000000000L
    //    val string = age.toString
    //    val ++---***/// = "htrr"
    //    var stryy = StdIn.readLine()
    //    println("str："+stryy)

    var a = 10
    var b = 20
    println("a=" + a + ",b=" + b)

    //中间值
    var c = a
    a = b
    b = c
    println("a=" + a + ",b=" + b)

    //相加
    var k = a + b
    b = k - b
    a = k - a
    println("a=" + a + ",b=" + b)


  }

}
