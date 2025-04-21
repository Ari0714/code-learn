package com.itbys.bigdata._06_wideClass


/**
  * Author Ari
  * Date 2023/3/14
  * Desc
  */
object _01_WideClass {

  def main(args: Array[String]): Unit = {

    f1(new Son1)

  }


  //包含本身 和 小的
  def f1[T <: Father1](t: T) {
    println(t)
  }


}


class GrandFather1 {}

class Father1 extends GrandFather1 {}

class Son1 extends Father1 {}
