package com.itbys.spark_basic.test

import com.itbys.spark_basic.util.{MyMySQLUtil, MyPGUtil}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
  * Author xx
  * Date 2024/5/5
  * Desc 
  */
object SparkHive {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder().config(conf).enableHiveSupport().getOrCreate()

    val tGoodsDF = MyMySQLUtil.readFromMysql(spark,"t_goods")
    tGoodsDF.createOrReplaceTempView("t_goods")
    tGoodsDF.show()

    spark.sql("set hive.exec.dynamic.partition.mode=nonstrict;")
    spark.sql(
      """
        |insert overwrite table mall_ods.ods_goods select *, '2019-09-14' dt from t_goods
      """.stripMargin)


  }

}
