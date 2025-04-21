package com.itbys.bigdata.scala.chapter04_collection

import com.sun.xml.internal.bind.v2.TODO

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Author xx
  * Date 2023/3/14
  * Desc
  */
object s2_seq {

  def main(args: Array[String]): Unit = {

    val list = List(1, 2, 3, 4)
    val list1 = List(5, 6, 7, 8)

    //取值
    println(list(1))

    //打印
    list.foreach(println)
    println(list.mkString(","))

    //增加 后
    val list3 = list :+ 1
    println(list3.mkString(","))

    //增加 前
    val list4 = 1 +: list
    println(list4.mkString(","))


    //默认不可变
    println(list == list4)

    //集合相加
    val list5 = list.++(list1)
    val list6 = list ++ list1
    println(list6.mkString(","))


    // ::
    val list7 = list.::(9)
    println(list7.mkString(","))

    val list8 = 7 :: 8 :: 9 :: list
    println(list8.mkString(","))

    val list9 = 7 :: list1 ::: list
    println(list9.mkString(","))

    //特殊集合Nil
    println(List())
    println(Nil)

    val list10 = 77 :: 88 :: 99 :: Nil
    println(list10.mkString(","))


    //修改
    val list11 = list1.updated(0, 1000)
    println(list11.mkString(","))

    //删除  从前往后  删除个数
    val list12 = list.drop(2)
    println(list12.mkString(","))


    // TODO 可变集合
    val mlist1 = ListBuffer(1, 2, 3, 4)


    //特别
    val head = mlist1.head
    println(head)

    val last = mlist1.last
    println(last)

    val tail = mlist1.tail
    println(tail.mkString(","))

    val init = mlist1.init
    println(init.mkString(","))


    // TODO queue
    //一定可变
    val qlist1 = mutable.Queue(1, 2, 3, 4)

    println(qlist1.mkString(","))

    //排队
    qlist1.enqueue(5)
    println(qlist1.mkString(","))

    //先进先出
    qlist1.dequeue()
    println(qlist1.mkString(","))


  }
}
