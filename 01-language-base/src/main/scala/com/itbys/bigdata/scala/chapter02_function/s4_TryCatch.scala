package com.itbys.bigdata.scala.chapter02_function

/**
  * Author xx
  * Date 2023/3/14
  * Desc
  */
object s4_TryCatch {

  def main(args: Array[String]): Unit = {

    try {
      val r = 10 / 0
    }
    catch {
      case ex: ArithmeticException => println("捕获了除数为零的算术异常")
      case ex: Exception => println("捕获了异常")
    } finally {
      // 最终要执行的代码
      println("scala finally...")
    }


  }

}
