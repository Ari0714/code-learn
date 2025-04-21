package com.itbys.bigdata._02_func

/**
  * Author Ari
  * Date 2023/3/14
  * Desc
  */
object _11_trait {

  def main(args: Array[String]): Unit = {
    val person1 = new Person11
    println(person1.a)

  }

}


trait test11 {
  println("aaa")
  val a: String = "aaaa"
}


trait tese11_1 {
  println("bbb")
  def test()
}


//继承，混入 继承父类，实现接口
class Person11 extends test11 with tese11_1 {

  //  a = "gbhfg"

  override def test(): Unit = {
    println("ggggg")
  }


}