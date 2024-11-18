package com.itbys.bigdata.scala.chapter03_implicit

/**
  * Author xx
  * Date 2023/3/14
  * Desc
  */
object s3_implicit_value {

  def main(args: Array[String]): Unit = {

    implicit val name: String = "coco"

    def getName(implicit name: String): Unit = {
      println(name)
    }

    //获取隐式值
    getName

  }


}
