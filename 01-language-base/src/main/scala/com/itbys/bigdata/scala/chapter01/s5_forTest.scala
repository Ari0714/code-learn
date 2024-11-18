package com.itbys.bigdata.scala.chapter01

import scala.io.StdIn

/**
  * Author xx
  * Date 2023/3/14
  * Desc
  */
object s5_forTest {

  def main(args: Array[String]): Unit = {

    //    test01()
    //    test02()
    //    test03()
    //    test04()
    //    test05
    //    test06()
    //    test07()
    //    demo01()
    demo02()

  }

  def demo02(): Unit = {
    println("请输入数字：")
    val a = StdIn.readInt()

    for (i <- 0 to a; j = a - i)
      println(i + " + " + j + " = " + a)

  }


  def demo01(): Unit = {
    val a = 0
    val ints = for (i <- 1 to 100 if i % 9 == 0) yield i

    println(ints.length, ints.sum)

  }

  //yield用法  把结果放进向量Vector
  def test07(): Unit = {

    val ints = for (i <- Range(1, 7, 1)) yield i + 1
    println(ints)

  }

  //Range用法 与 until类似
  def test06(): Unit = {

    for (i <- Range(1, 7, 1)) {
      println(i)
    }

  }

  //循环嵌套;隔断逻辑 (2,3,4) x (3,4,5)
  def test05(): Unit = {
    for (i <- 2 until 5; j <- 3 until 6) {
      println(i)
      println(j)
      println("eeeeeeeeeeeee")
    }

  }

  //引入变量 ;隔断逻辑
  def test04(): Unit = {
    for (i <- 2 until 5; j = i + 1) {
      println(i)
      println(j)
      println("eeeeeeeeeeeee")
    }

  }


  //循环守卫,排除
  def test03(): Unit = {
    for (i <- 2 until 5 if i != 3) {
      println(i)
      println("eeeeeeeeeeeee")
    }
  }

  //后不包含  与java i<a.lenth类似
  def test02(): Unit = {
    for (i <- 2 until 5)
      println("eeeeeeeeeeeee")

  }

  //前后包含
  def test01(): Unit = {
    for (i <- 2 to 10)
      println(i)

  }

}
