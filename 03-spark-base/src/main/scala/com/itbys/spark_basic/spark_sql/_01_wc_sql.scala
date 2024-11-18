package com.itbys.spark_basic.spark_sql

import com.itbys.spark_basic.bean.UserVisitAction
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}

object _01_wc_sql {

  def main(args: Array[String]): Unit = {

    //1.创建
    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder().config(conf).enableHiveSupport().getOrCreate()
    import spark.implicits._

    //UserVisitAction(2019-07-22,53,43f2fe02-e28d-4251-acbc-51569e0d6642,49,2019-07-22 09:57:04,null,-1,-1,19_6_14_11,52_79_98,null,null,3)
    //                             date: String,//用户点击行为的日期
    //                             user_id: Long,//用户的ID
    //                             session_id: String,//Session的ID
    //                             page_id: Long,//某个页面的ID
    //                             action_time: String,//动作的时间点
    //                             search_keyword: String,//用户搜索的关键词
    //                             click_category_id: Long,//某一个商品品类的ID
    //                             click_product_id: Long,//某一个商品的ID
    //                             order_category_ids: String,//一次订单中所有品类的ID集合
    //                             order_product_ids: String,//一次订单中所有商品的ID集合
    //                             pay_category_ids: String,//一次支付中所有品类的ID集合
    //                             pay_product_ids: String,//一次支付中所有商品的ID集合
    //                             city_id: Long //城市 id
    val userVisitActionRDD: RDD[UserVisitAction] = spark.sparkContext
      .textFile("input/user_visit_action.txt")
      .map { x =>
        val strings: Array[String] = x.split("_")

        if (strings(8) != "null")
          strings(8) = strings(8).replaceAll(",", "_")
        if (strings(9) != "null")
          strings(9) = strings(9).replaceAll(",", "_")
        if (strings(10) != "null")
          strings(10) = strings(10).replaceAll(",", "_")
        if (strings(11) != "null")
          strings(11) = strings(11).replaceAll(",", "_")

        UserVisitAction(strings(0), strings(1).toLong, strings(2), strings(3).toLong, strings(4),
          strings(5), strings(6).toLong, strings(7).toLong, strings(8), strings(9),
          strings(10), strings(11), strings(12).toLong)

      }


    /**
      * 2.RDD df ds装换
      */
    //rdd => df ds
    val fr: DataFrame = userVisitActionRDD.toDF()
    val ds: Dataset[UserVisitAction] = userVisitActionRDD.toDS()

    //df ds => rdd
    val rdd01: RDD[UserVisitAction] = fr.as[UserVisitAction].rdd
    val rdd02: RDD[UserVisitAction] = ds.rdd

    //df <==> ds
    val ds02: Dataset[UserVisitAction] = fr.as[UserVisitAction]
    val fr02: DataFrame = ds.toDF()


    /**
      * 3.sql操作
      */
    val userVisitActionFrame: DataFrame = userVisitActionRDD.toDF()
    userVisitActionFrame.createOrReplaceTempView("test")

    val resultDF: DataFrame = spark.sql("select page_id,count(*) cc from test group by page_id order by cc desc limit 10")
    resultDF.show()

    /**
      * 4.自定义udf函数
      */
    spark.udf.register("add", (name: String) => "name: " + name)
    spark.sql("select add(user_id) from test").show()

  }

}
