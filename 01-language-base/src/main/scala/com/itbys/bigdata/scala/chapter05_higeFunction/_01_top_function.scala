package com.itbys.bigdata.scala.chapter05_higeFunction

import scala.collection.mutable.ArrayBuffer

/**
  * Author Ari
  * Date 2023/3/14
  * Desc
  */
object _01_top_function {

  def main(args: Array[String]): Unit = {

    // TODO: 1.普通函数
    def fun1() = "aaa"

    val str01 = fun1()
    println(str01)

    val str02 = fun1 _
    println(str02)


    // TODO: 2.闭包
    // _表示引用函数
    // 内部函数引用外部函数的变量，改变了变量的声明周期，称之为闭包。柯里化一定有闭包
    def fun6(i: Int) = {

      def fun66(j: Int) = i * j

      fun66 _
    }

    println(fun6(5)(6))

    //todo 3.柯里化 ，简化
    def fun7(i: Int)(j: Int) = i * j

    println(fun7(6)(6))
  }

  private val buffer = ArrayBuffer()

}
