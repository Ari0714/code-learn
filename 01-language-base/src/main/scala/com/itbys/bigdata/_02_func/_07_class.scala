package com.itbys.bigdata._02_func

/**
  * Author Ari
  * Date 2023/3/14
  * Desc
  */
object _07_class {

  def main(args: Array[String]): Unit = {
    val user0 = new User07
    user0.test()
  }

}


abstract class Person07 {

  //定义 _  必须用var
  var name: String = _

  def test()
}


class User07 extends Person07 {

  override def test(): Unit = {
    println("ffff")
  }
}

