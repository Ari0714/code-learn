package com.itbys.spark_basic.spark_streaming._02_test

import java.sql.{Connection, ResultSet}
import java.text.SimpleDateFormat

import com.itbys.spark_basic.bean.AdsLog
import com.itbys.spark_basic.util.MyKafkaUtil
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}


object _01_blackList {

  def main(args: Array[String]): Unit = {

    val topic = "spark_stream_test"

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(5))

    // TODO: 1.获取kafka数据
    val kafkaDS: DStream[String] = MyKafkaUtil.getKafkaStream(ssc, topic).map(_.value())

    val adslogDS: DStream[AdsLog] = kafkaDS.map { x =>
      val strings: Array[String] = x.split(" ")
      AdsLog(strings(0).toLong, strings(1), strings(2), strings(3), strings(4))
    }


    // TODO: 2.过滤正常用户
    val filterDS: DStream[AdsLog] = adslogDS.transform { rdd =>
      val blacklists: Array[Long] = JdbcUtil.findAll()
      rdd.filter { x => !blacklists.contains(x.userid.toLong) }
    }

    // TODO: 3.统计流中的点击
    val clickCnt: DStream[((String, String, String), Int)] = filterDS.map { x =>
      val format: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
      val dateStr: String = format.format(x.timestamp)
      ((dateStr, x.userid, x.adid), 1)
    }
      .reduceByKey(_ + _)


    // TODO: 4.流与数据库合并
    clickCnt.foreachRDD { rdd => {
      rdd.foreachPartition { iter => {

        val list: List[((String, String, String), Int)] = iter.toList

        val connection: Connection = JdbcUtil.getConnection

        for (elem <- list) {
          val dt: String = elem._1._1
          val uid: String = elem._1._2
          val aid: String = elem._1._3
          val cnt: Int = elem._2

          val dbCnt: Long = JdbcUtil.findSingleClickCount(connection, dt, uid, aid)
          val newCnt: Long = cnt + dbCnt

          JdbcUtil.updateSingleClickCount(connection, dt, uid, aid, newCnt)

          //加入黑名单
          val newDbCnt: Long = JdbcUtil.findSingleClickCount(connection, dt, uid, aid)
          if (newDbCnt > 30) {
            JdbcUtil.addBlackList(connection, uid)
          }

        }
      }
      }
    }
    }


    ssc.start()
    ssc.awaitTermination()

  }

}
