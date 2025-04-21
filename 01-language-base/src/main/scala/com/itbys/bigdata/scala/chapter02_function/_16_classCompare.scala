package com.itbys.bigdata.scala.chapter02_function

/**
  * Author Ari
  * Date 2023/3/14
  * Desc
  */
object _16_classCompare {

  def main(args: Array[String]): Unit = {

    val aa = new AA16

    val bool = aa.isInstanceOf[AA16]

    if (bool) {
      val aa = bool.asInstanceOf[AA16]
    }


  }

}


class AA16 {


}
