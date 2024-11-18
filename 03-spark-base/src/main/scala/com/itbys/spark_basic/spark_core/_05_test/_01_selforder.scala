package com.itbys.spark_basic.spark_core._05_test

import com.itbys.spark_basic.bean.UserVisitAction
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object _01_selforder {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val sc = new SparkContext(conf)

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
    val userVisitActionRDD: RDD[UserVisitAction] = sc.textFile("input/user_visit_action.txt")
      .map {
        x => {
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
      }

    //后面重复使用用cache
    userVisitActionRDD.cache()


    // TODO: 1.cogroup  有shuffle
    //    val clickCount: RDD[(String, Int)] = userVisitActionRDD.map(x => (x.click_category_id.toString, 1)).filter(_._1 != "-1").reduceByKey(_ + _)
    //
    //    val orderCount: RDD[(String, Int)] = userVisitActionRDD.map(_.order_category_ids)
    //      .filter(_ != "null")
    //      .flatMap(_.split("_"))
    //      .map((_, 1))
    //      .reduceByKey(_ + _)
    //
    //    val payCount: RDD[(String, Int)] = userVisitActionRDD.map(_.pay_category_ids)
    //      .filter(_ != "null")
    //      .flatMap(_.split("_"))
    //      .map((_, 1))
    //      .reduceByKey(_ + _)

    //    val coRDD: RDD[(String, (Iterable[Int], Iterable[Int], Iterable[Int]))] = clickCount.cogroup(orderCount,payCount)

    //    val cntRDD: RDD[(String, (Int, Int, Int))] = coRDD.mapValues {
    //      case (a, b, c) =>
    //        var clickCnt = 0
    //        var orderCnt = 0
    //        var payCnt = 0
    //
    //        if (a.iterator.hasNext)
    //          clickCnt = a.iterator.next()
    //        if (b.iterator.hasNext)
    //          orderCnt = b.iterator.next()
    //        if (c.iterator.hasNext)
    //          payCnt = c.iterator.next()
    //
    //        (clickCnt, orderCnt, payCnt)
    //    }

    // TODO: 2.union  大量shuffle操作
    //    val clickU: RDD[(String, (Int, Int, Int))] = clickCount.map(x => (x._1, (x._2, 0, 0)))
    //    val orderU: RDD[(String, (Int, Int, Int))] = orderCount.map(x => (x._1, (0, x._2, 0)))
    //    val payU: RDD[(String, (Int, Int, Int))] = payCount.map(x => (x._1, (0, 0, x._2)))
    //
    //    val unionRDD: RDD[(String, (Int, Int, Int))] = clickU.union(orderU).union(payU)
    //    val cntRDDU: RDD[(String, (Int, Int, Int))] = unionRDD
    //      .reduceByKey(
    //        (x, y) => (x._1 + y._1, x._2 + y._2, x._3 + y._3)
    //      )

    // TODO: 3.一开始shuffle
    val clickC: RDD[(String, (Int, Int, Int))] = userVisitActionRDD.map(x => (x.click_category_id.toString, (1, 0, 0))).filter(_._1 != "-1")

    val orderC: RDD[(String, (Int, Int, Int))] = userVisitActionRDD.map(_.order_category_ids)
      .filter(_ != "null")
      .flatMap(_.split("_"))
      .map((_, (0, 1, 0)))

    val payC: RDD[(String, (Int, Int, Int))] = userVisitActionRDD.map(_.pay_category_ids)
      .filter(_ != "null")
      .flatMap(_.split("_"))
      .map((_, (0, 0, 1)))

    val cnt: RDD[(String, (Int, Int, Int))] = clickC.union(orderC).union(payC)
      .reduceByKey(
        (x, y) => (x._1 + y._1, x._2 + y._2, x._3 + y._3)
      )

    // TODO: 4.使用累加器避免shuffle


    //元祖自动顺序排序  排序带分区设置 1  collect拉到driver全局有序
    cnt.sortBy(_._2, false).take(10).foreach(println(_))


  }

}