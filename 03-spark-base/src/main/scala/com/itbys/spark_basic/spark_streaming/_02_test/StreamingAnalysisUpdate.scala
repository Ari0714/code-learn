package com.itbys.spark_basic.spark_streaming._02_test

import com.itbys.spark_basic.util.{MyKafkaUtil, MyMySQLUtil}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Author xx
  * Date 2023/12/14
  * Desc 
  */
object StreamingAnalysisUpdate {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getName).setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(5))
    ssc.checkpoint("ck")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    import spark.implicits._

    val topic = "ratings_topic"
    val groupId = "consumer_01"

    val inputKafkaDS: InputDStream[ConsumerRecord[String, String]] = MyKafkaUtil.getKafkaStream(ssc, topic, groupId)

    //获取每个评分
    val modelDS: DStream[String] = inputKafkaDS.map(_.value())
      .filter(x => x.trim.length > 0 && !x.contains("userId"))
      .map { x =>
        x.split(",")(2)
      }

    //累计统计
    val resultDS: DStream[(String, Long)] = modelDS
      .map(x => (x, 1L))
      .updateStateByKey {
        case (values: Seq[Long], old: Option[Long]) =>
          var cnt = 0L
          if (old.isDefined)
            cnt = old.get
          for (elem <- values) {
            cnt += elem
          }
          Option(cnt)
      }
    resultDS.print()

    //保存到mysql
    resultDS.foreachRDD(rdd => MyMySQLUtil.saveToMysql(rdd.toDF("rating", "cnt"), "rating_cnt"))


    ssc.start()
    ssc.awaitTermination()


  }


}
