package com.itbys.bigdata.scala.chapter04_collection

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Author xx
  * Date 2023/3/14
  * Desc
  */
object s1_test {

  def main(args: Array[String]): Unit = {

    val ints = Array(1, 2, 3, 4)

    //    for (elem <- ints.filter(x -> x % 2 != 0)) {
    //      println(i)
    //    }

    val ints2 = ints.map(x => x + 5)
    //    for (i <- ints2)
    //      println(i)

    println(ints.sum)

    println(ints.length)

    //取值（从0开始）
    println(ints(1))

    //length
    println(ints.length)

    //mkString 默认没有分割
    println(ints.mkString(","))

    //2 打印
    for (i <- ints)
      println(i)

    //3
    def xxx(i: Int): Unit = {
      println(i)
    }

    ints.foreach(xxx)

    //3
    ints.foreach((i: Int) => {
      println(i)
    }) //匿名函数
    ints.foreach((i) => {
      println(i)
    }) //自动推断
    ints.foreach({
      println(_)
    }) // 只用一次，可以不重复写
    ints.foreach(println(_)) // 只一行，{}省
    println("----------")
    ints.foreach(println) //参数只一个，可以省

    //update
    ints.update(0, 9)
    ints.foreach(println)

    //insert01  :贴ints
    println()
    val ints1 = ints :+ 5
    ints1.foreach(println)

    //insert02  :贴ints
    println()
    //    val ints2: Array[Int] = 5 +: ints
    //    ints2.foreach(println)


    //可变数组
    println("=======================================")

    val intsb = ArrayBuffer(1, 2, 3, 4, 5)

    //取值
    println(intsb(1))

    //mkString
    println(intsb.mkString(","))

    //循环
    intsb.foreach(println)

    //增加
    intsb.insert(0, 0)
    intsb.foreach(println)

    //增加 末尾加
    println()
    intsb += 0
    intsb.foreach(println)

    //删除  下表，count  返回删除的值
    println()
    val i = intsb.remove(1)
    intsb.foreach(println)
    println(i)


    //可变不可变转化
    val buffer = ints.toBuffer

    val array = intsb.toArray


  }


}
