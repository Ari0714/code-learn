package com.itbys.bigdata.scala.chapter05_higeFunction

import scala.collection.mutable.ArrayBuffer

/**
  * Author Ari
  * Date 2023/3/14
  * Desc
  */
object s1 {

  def main(args: Array[String]): Unit = {

    //    s1()

    //    s2()

    //    s3()

    //    s8()

    //    s09()

    //    s10()

  }


  //柯里化 两数相乘
  def s10(): Unit = {

    //普通
    def f1(x: Int, y: Int) = x * y

    //闭包
    def f2(x: Int) = (y: Int) => x * y

    //柯里化
    def f3(x: Int)(y: Int) = x * y

    println(f1(10, 20))
    println(f2(10)(20))
    println(f3(10)(20))

  }

  //demoTest  闭包文件
  def s09(): Unit = {

    def ss(s: String) = (fName: String) => s + "." + fName

    val stringToString = ss("aa")

    println(stringToString)

  }


  //闭包
  def s8(): Unit = {

    //ss返回匿名函数 (y: Int) => x+y   先填外，再填内
    def ss(x: Int) = (y: Int) => x + y

    val f = ss(20)

    println(f(1))


  }

  //作为参数的函数
  def s7(): Unit = {

    //一行{} 可以省
    def f1(int: Int) = int + 1

    println(Array(1, 2, 3, 4).map(f1(_)))

  }


  //至简
  def s6(): Unit = {

    List(1, 2, 3, 4, "abc").collect { case x: Int => x + 1 }

  }

  //case 语句自动转化偏函数
  def s5(): Unit = {
    val list = List(1, 2, 3, 4, "abc")

//    def ss = {
//      case x: Int => x + 1
//    }

//    list.collect(ss)
  }


  //partial function  只能是collect [Any, Int]传入类型，返回类型 isDefineAt => true => apply
  def s4(): Unit = {

    val list = List(1, 2, 3, 4, "abc")
    //说明
    val addOne3 = new PartialFunction[Any, Int] {
      def isDefinedAt(any: Any) = if (any.isInstanceOf[Int]) true
      else false

      def apply(any: Any) = any.asInstanceOf[Int] + 1
    }
    val list3 = list.collect(addOne3)
    println("list3=" + list3)

  }


  //demo3 模式匹配 case
  def s3(): Unit = {

    def add1(a: Any) = {
      a match {
        case x: Int => x + 1
        case _ =>
      }

      val list = List(1, 2, 3, 4, "abc")
//      val list2 = list.map(add1)
//      println("list2=" + list2)

    }

  }


  //demo2 partial function
  def s2(): Unit = {

    val list = List(1, 2, 3, 4, "abc")

    def f1(n: Any) = n.isInstanceOf[Int]

    def f2(n: Int) = n + 1

    def f3(n: Any) = n.asInstanceOf[Int]

    val list2 = list.filter(f1).map(f3).map(f2)
    println("list2=" + list2)

  }


  //demo1
  def s1(): Unit = {

    // => 得到(2,3,4,5)
    val list = List(1, 2, 3, 4, "gggg")

    //filter过滤出来
    val ints = list.filter(_.isInstanceOf[Int]).map(_.asInstanceOf[Int]).map(_ + 1)

    println(ints.mkString(","))

  }

}
