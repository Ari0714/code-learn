package com.itbys.bigdata.scala.chapter02_function

/**
  * Author xx
  * Date 2023/3/14
  * Desc
  */
object s12_trait {

  def main(args: Array[String]): Unit = {

    val person1 = new Person12

  }

}


trait test12 {

  println("aaa")

}

trait tese12_1 {
  println("bbb")

}


class test12_2 {
  println("class主")
}

//继承，混入 继承父类，实现接口
class Person12 extends test12_2 with test12 with tese12_1 {
  println("current")

}