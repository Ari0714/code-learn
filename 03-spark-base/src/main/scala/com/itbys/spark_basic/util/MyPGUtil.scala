package com.itbys.spark_basic.util

import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

/**
  * Author xx
  * Date 2024/1/26
  * Desc 
  */
object MyPGUtil {

  /**
    * @desc 读取pg
    * @param: sparkSession
    * @param: table
    * @return: org.apache.spark.sql.Dataset<org.apache.spark.sql.Row>
    */
  def readFromPG(sparkSession: SparkSession, table: String) = {
    sparkSession.read
      .format("jdbc")
      .option("driver", "org.postgresql.Driver")
      .option("url", "jdbc:postgresql://hdp:5432/geodb")
      .option("user", "root")
      .option("password", "111111")
      .option("dbtable", table)
      .load()
  }


  /**
    * @desc 写入pg
    * @param: dataFrame
    * @param: table
    */
  def saveToPG(dataFrame: DataFrame, table: String) = {
    dataFrame.write
      .format("jdbc")
      .option("driver", "org.postgresql.Driver")
      .option("url", "jdbc:postgresql://hdp:5432/geodb")
      .option("user", "root")
      .option("password", "111111")
      .option("dbtable", table)
      .mode(SaveMode.Overwrite)
      .save()
  }

  /**
   * @desc 写入pg
   * @param: dataFrame
   * @param: table
   * @param: url
   * @param: username
   * @param: password
   */
  def saveToPG(dataFrame: DataFrame, table: String,url: String, username: String, password: String) = {
    dataFrame.write
      .format("jdbc")
      .option("driver", "org.postgresql.Driver")
      .option("url", url)
      .option("user", username)
      .option("password", password)
      .option("dbtable", table)
      .mode(SaveMode.Overwrite)
      .save()
  }

}
