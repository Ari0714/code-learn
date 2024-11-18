package com.itbys.spark_basic.spark_core._05_test

import com.itbys.spark_basic.bean.UserVisitAction
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object _03_jump {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val sc = new SparkContext(conf)

    // TODO: 1.获取数据
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
    userVisitActionRDD.cache()

    // TODO: 2. 规定的跳转
    val ints = List(1, 2, 3, 4, 5, 6, 7)
    val standardJump: List[String] = ints.zip(ints.tail).map { case (a, b) => a + "_" + b }
    //    standardJump.foreach(println(_))

    // TODO: 3.过滤 分组 排序
    val filterRDD: RDD[UserVisitAction] = userVisitActionRDD.filter { x => ints.contains(x.page_id) }

    val sortRDD: RDD[(String, List[UserVisitAction])] = filterRDD.groupBy(_.session_id)
      .mapValues { iter =>
        iter.toList.sortWith((a, b) => a.action_time < b.action_time)
      }

    val jumpRDD: RDD[(String, List[String])] = sortRDD.mapValues { iter =>
      val strings: List[String] = iter.map(_.page_id).zip(iter.map(_.page_id).tail)
        .map {
          case (a, b) => a + "_" + b
        }
      strings
    }

    val upRDD: RDD[(String, Int)] = jumpRDD
      .map(x => x._2)
      .flatMap(x => x)
      .filter(x => standardJump.contains(x))
      .map((_, 1))
      .reduceByKey(_ + _)

    val downmap: Map[String, Int] = filterRDD.map(x => (x.page_id.toString, 1))
      .reduceByKey(_ + _)
      .collect().toMap

    //除用 double  int 四舍五入为0
    upRDD.foreach {
      case (pids, sum) =>
        val fromPage: String = pids.split("_")(0)
        val downcnt: Int = downmap.getOrElse(fromPage, 1)
        println(pids + "=" + (sum.toDouble / downcnt))
    }

    //    downRD.foreach(println(_))


  }

}
