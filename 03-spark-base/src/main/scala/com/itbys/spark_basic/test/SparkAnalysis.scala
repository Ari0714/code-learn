package com.itbys.spark_basic.test

import com.itbys.spark_basic.util.MyMySQLUtil
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

/**
  * Author xx
  * Date 2022/7/31
  * Desc 
  */
object SparkAnalysis {

  def main(args: Array[String]): Unit = {


  }


  /**
    * @desc 读取csv
    */
  def readFromCsv() = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder.config(conf).getOrCreate()

    val mySchema = StructType(Array(
      StructField("id", StringType),
      StructField("content_id", StringType),
      StructField("page_path", StringType),
      StructField("userid", StringType),
      StructField("sessionid", StringType),
      StructField("date_time", StringType)
    ))

    //读取数据
    val inputDF = spark.read
      .format("csv")
      .option("header", "false")
      .schema(mySchema)
      .option("sep", "\t")
      .load("output/small_train_format/part-00000")
    inputDF.show()
    inputDF.createOrReplaceTempView("rent_info")


  }


  /**
    * @desc 读取Xlsx
    */
  def readFromXlsx() = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder.config(conf).getOrCreate()

    //读取数据
    val inputDF: DataFrame = spark.read
      .format("com.crealytics.spark.excel")
      .option("useHeader", "true")
      .load("input/hangzhou_rent.xlsx")
    inputDF.show()
    inputDF.createOrReplaceTempView("rent_info")

    val resDF02 = spark.sql(
      """
        |select product, sum(replace(replace(replace(comment4,'条评价',''),'+',''),'万','0000')) sales
        |from jd_sales
        |group by product
        |order by sales
        |limit 10
      """.stripMargin)
    resDF02.show()

    //存储到本地
    resDF02.repartition(1).write.mode(SaveMode.Overwrite).option("header", "true").json("hdfs://hdp:8020/output/02")

    //存储到数据库
    MyMySQLUtil.saveToMysql(resDF02,"resDF02")

  }


}
