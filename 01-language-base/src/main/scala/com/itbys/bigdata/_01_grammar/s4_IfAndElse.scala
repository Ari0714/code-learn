package com.itbys.bigdata._01_grammar

import scala.io.StdIn

/**
  * Author xx
  * Date 2023/3/14
  * Desc
  */
object s4_IfAndElse {

  def main(args: Array[String]): Unit = {

    ifRetunrn()
    threeM()
    demo01()
    demo02()

  }

  //案例2
  def demo02(): Unit = {

    println("请输入成绩:")
    val i = StdIn.readInt()

    println("请输入性别:")
    val str = StdIn.readLine()

    if (i >= 80) if ("man".equals(str))
      println("进入男子决赛")
    else
      println("进入女子决赛") else println("淘汰")

  }

  //案例
  def demo01(): Unit = {

    val a = 9
    val unit = if (a > 10)
      "aaaaa"
    println(unit)

  }

  //三元
  def threeM(): Unit = {

    val aa = "aa"
    val unit = if (aa.equals("aa")) 1 else 0
    println(unit)

  }

  //if返回值
  def ifRetunrn(): Unit = {

    val aa = true
    val unit = if (aa) "dsfs" else "hfghgf"

    println(unit)

  }
}
