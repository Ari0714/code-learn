package com.itbys.bigdata.scala.chapter03_implicit

/**
  * Author xx
  * Date 2023/3/14
  * Desc
  */
object s02_InsDel {

  def main(args: Array[String]): Unit = {

    implicit def chage(mySQL: MySQL) = new DB

    //隐式转换包含自己和转化来的方法属性
    val mySQL = new MySQL

    mySQL.insert
    mySQL.update

  }

}

class MySQL {
  def insert: Unit = {
    println("insert")
  }
}

class DB {
  def update: Unit = {
    print("update")
  }
}