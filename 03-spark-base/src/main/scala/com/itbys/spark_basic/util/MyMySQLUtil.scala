package com.itbys.spark_basic.util

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

/**
  * Author xx
  * Date 2022/7/31
  * Desc 
  */
object MyMySQLUtil {

  /**
    * @desc 读取mysql
    * @param: sparkSession
    * @param: table
    * @return: org.apache.spark.sql.Dataset<org.apache.spark.sql.Row>
    */
  def readFromMysql(sparkSession: SparkSession, table: String) = {
    sparkSession.read
      .format("jdbc")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("url", "jdbc:mysql://hdp:3306/test?characterEncoding=utf-8&useSSL=false")
      .option("user", "root")
      .option("password", "111111")
      .option("dbtable", table)
      .load()
  }


  /**
   * @desc 读取mysql
   * @param: sparkSession
   * @param: table
   * @param: url
   * @param: username
   * @param: password
   * @return: org.apache.spark.sql.Dataset<org.apache.spark.sql.Row>
   */
  def readFromMysql(sparkSession: SparkSession, table: String, url: String, username: String, password: String) = {
    sparkSession.read
      .format("jdbc")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("url", url)
      .option("user", username)
      .option("password", password)
      .option("dbtable", table)
      .load()
  }


  /**
    * @desc 写入mysql
    * @param: dataFrame
    * @param: table
    */
  def saveToMysql(dataFrame: DataFrame, table: String) = {
    dataFrame.write
      .format("jdbc")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("url", "jdbc:mysql://hdp:3306/test?characterEncoding=utf-8&useSSL=false")
      .option("user", "root")
      .option("password", "111111")
      .option("dbtable", table)
      .mode(SaveMode.Overwrite)
      .save()
  }


}
