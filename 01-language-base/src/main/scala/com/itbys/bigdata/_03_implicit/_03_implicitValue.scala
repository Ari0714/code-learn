package com.itbys.bigdata._03_implicit

/**
  * Author Ari
  * Date 2023/3/14
  * Desc
  */
object _03_implicitValue {

  def main(args: Array[String]): Unit = {

    implicit val name: String = "coco"

    def getName(implicit name: String): Unit = {
      println(name)
    }

    //获取隐式值
    getName

  }


}
