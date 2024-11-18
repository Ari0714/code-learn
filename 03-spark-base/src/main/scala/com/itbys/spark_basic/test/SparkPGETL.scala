package com.itbys.spark_basic.test

import com.itbys.spark_basic.util.{MyMySQLUtil, MyPGUtil}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
  * Author xx
  * Date 2024/1/26
  * Desc 
  */
object SparkPGETL {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName)//.setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder.config(conf).getOrCreate()

    val frame = MyMySQLUtil.readFromMysql(spark, args(0),
      "jdbc:mysql://10.126.124.44:4000/data_asset?characterEncoding=utf-8&useSSL=false",
      "dataasset",
      "Da@20232")
    frame.repartition(200)
    frame.show()

    MyPGUtil.saveToPG(frame,"temp."+args(0),
    "jdbc:postgresql://hgprecn-cn-tl32n6qol005-cn-hangzhou.hologres.aliyuncs.com:80/bigdatadwcn",
    "BASIC$tmp_dtc_user01",
      "M2yQkh7ccUdzV9Nc")

    spark.stop()

  }

}
