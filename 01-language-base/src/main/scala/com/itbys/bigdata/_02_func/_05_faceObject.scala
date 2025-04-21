package com.itbys.bigdata._02_func

/**
  * Author xx
  * Date 2023/3/14
  * Desc
  */
object _05_faceObject {

  def main(args: Array[String]): Unit = {

    val person = new Person

    person.name = "coco"

    person.age = 77

    println(person.name + "----" + person.age)

    person.test()

  }

}


class Person {

  //val 底层final
  var eamil: String = _

  var name: String = _

  var age: Int = _


  def test(): Unit = {
    println("aaaa")
  }

}
