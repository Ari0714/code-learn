package com.itbys.bigdata._03_implicit

/**
  * Author Ari
  * Date 2023/3/14
  * Desc
  */
object _02_insDel {

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