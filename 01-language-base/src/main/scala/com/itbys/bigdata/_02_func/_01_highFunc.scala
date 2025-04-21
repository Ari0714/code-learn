package com.itbys.bigdata._02_func

/**
  * Author Ari
  * Date 2025/4/21
  * Desc 
  */
object _01_highFunc {


  def main(args: Array[String]): Unit = {

    /**
      * 高阶函数，1. 参数为函数，2.返回结果为函数
      */
    //1. 返回为函数
    val f1 = (a: String) => a + 1


    //2. 参数为函数
    def test(x: Int) = {
      x + 100
    }

    def f2(x: Int, func: Int => Int) = {
      func(x)
    }

    val res = f2(1, test)
    println(res)



  }


}
