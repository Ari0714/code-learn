package com.itbys.spark_basic.spark_streaming._02_test

import java.sql.{Connection, PreparedStatement, ResultSet}
import java.util.Properties

import javax.sql.DataSource
import com.alibaba.druid.pool.DruidDataSourceFactory
import com.itbys.spark_basic.bean.AdUserClickCount
import com.itbys.spark_basic.util.MyPropertiesUtil

import scala.collection.mutable.ArrayBuffer

object JdbcUtil {

  //初始化连接池
  var dataSource: DataSource = init()

  //初始化连接池方法
  def init(): DataSource = {
    val properties = new Properties()
    val config: Properties = MyPropertiesUtil.load("03-spark-base/input/config.properties")
    properties.setProperty("driverClassName", "com.mysql.jdbc.Driver")
    properties.setProperty("url", config.getProperty("jdbc.url"))
    properties.setProperty("username", config.getProperty("jdbc.user"))
    properties.setProperty("password", config.getProperty("jdbc.password"))
    properties.setProperty("maxActive", config.getProperty("jdbc.datasource.size"))
    DruidDataSourceFactory.createDataSource(properties)
  }

  //获取MySQL连接
  def getConnection: Connection = {
    dataSource.getConnection
  }

  //查询所有黑名单义用户
  def findAll(): Array[Long] = {
    // 将黑名单中的所有数据查询出来
    val sql = "SELECT * FROM black_list"

    val adBlacklists = new ArrayBuffer[Long]()

    // 获取对象池单例对象
    val connection: Connection = getConnection

    // 执行sql查询并且通过处理函数将所有的userid加入array中
    val resultSet: ResultSet = connection.prepareStatement(sql).executeQuery()

    while (resultSet.next()) {
      val userid: Long = resultSet.getInt(1).toLong
      adBlacklists += userid
    }

    adBlacklists.toArray

  }

  //加入黑名单义
  def addBlackList(connection: Connection, uid: String) = {
    // 将黑名单中的所有数据查询出来
    val sql = "insert into black_list VALUES ('" + uid + "')"

    try {
      connection.prepareStatement(sql).executeUpdate()
    } catch {
      case e => e.printStackTrace()
    }

  }


  //查询所有用户的点击
  def findClickCount() = {
    // 将黑名单中的所有数据查询出来

    val sql = "SELECT * FROM user_ad_count"

    val adUserClickCount = new ArrayBuffer[AdUserClickCount]()

    // 获取对象池单例对象
    val connection: Connection = getConnection

    // 执行sql查询并且通过处理函数将所有的userid加入array中
    val resultSet: ResultSet = connection.prepareStatement(sql).executeQuery()

    while (resultSet.next()) {
      val dt: String = resultSet.getString(1)
      val userid: String = resultSet.getString(2)
      val adid: String = resultSet.getString(3)
      val cnt: Long = resultSet.getInt(4).toLong

      adUserClickCount += AdUserClickCount(dt, userid, adid, cnt)
    }

    adUserClickCount.toArray

  }


  //查询单个用户的点击
  def findSingleClickCount(connection: Connection, dt: String, uid: String, aid: String) = {
    // 将黑名单中的所有数据查询出来

    val sql = "SELECT count from user_ad_count where dt = '" + dt + "' and userid = '" + uid + "' and adid = '" + aid + "'"

    var cnt = 0L

    // 执行sql查询并且通过处理函数将所有的userid加入array中
    val resultSet: ResultSet = connection.prepareStatement(sql).executeQuery()

    while (resultSet.next()) {
      cnt = resultSet.getInt(1).toLong
    }
    cnt
  }


  //更新单个用户的点击
  def updateSingleClickCount(connection: Connection, dt: String, uid: String, aid: String, newCnt: Long) = {
    // 将黑名单中的所有数据查询出来

    val sql = "update user_ad_count set count = " + newCnt + " where dt = '" + dt + "' and userid = '" + uid + "' and adid = '" + aid + "'"

    // 执行sql查询并且通过处理函数将所有的userid加入array中
    var i: Long = 0L

    try {
      i = connection.prepareStatement(sql).executeUpdate()
    } catch {
      case e => e.printStackTrace()
    }

    if (i == 0) {
      val sql = "insert into user_ad_count VALUES ('" + dt + "','" + uid + "','" + aid + "'," + newCnt + ")"
      connection.prepareStatement(sql).executeUpdate()
    }

  }

  //主方法,用于测试上述方法
  def main(args: Array[String]): Unit = {

    println(findAll().length)

  }


}
