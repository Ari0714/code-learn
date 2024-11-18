package com.itbys.spark_basic.util

import java.util.Properties

import com.itbys.spark_basic.common.CommonConfig
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{HBaseConfiguration, HTableDescriptor, TableName}
import org.apache.hadoop.hbase.client.{HBaseAdmin, HTable, Put}
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.kafka010._

/**
  * Author xx
  * Date 2023/12/26
  * Desc 读写hbase
  */
object MyHbaseUtil {

  /**
   * @desc 设置HBaseConfiguration
   * @param: quorum
   * @param: port
   * @param: tableName
   * @return: org.apache.hadoop.conf.Configuration
   */
  def getHBaseConfiguration(quorum: String, port: String, tableName: String) = {
    val conf = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.quorum", quorum)
    conf.set("hbase.zookeeper.property.clientPort", port)

    conf
  }

  /**
   * @desc 返回或新建HBaseAdmin
   * @param: conf
   * @param: tableName
   * @return: org.apache.hadoop.hbase.client.HBaseAdmin
   */
  def getHBaseAdmin(conf: Configuration, tableName: String) = {
    val admin = new HBaseAdmin(conf)
    if (!admin.isTableAvailable(tableName)) {
      val tableDesc = new HTableDescriptor(TableName.valueOf(tableName))
      admin.createTable(tableDesc)
    }

    admin
  }

  /**
   * @desc 返回HTable
   * @param: conf
   * @param: tableName
   * @return: org.apache.hadoop.hbase.client.HTable
   */
  def getTable(conf: Configuration, tableName: String) = {
    new HTable(conf, tableName)
  }


  //写入hbase
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getName).setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()
    import spark.implicits._

    val inputRDD: RDD[(String, Int)] = spark.sparkContext.makeRDD(Array(("coco", 12)))

    inputRDD.foreach { x =>
      val hbaseTable = "city_table"

      val hbaseConf: Configuration = MyHbaseUtil.getHBaseConfiguration("hdp", "2181", hbaseTable)
      hbaseConf.set(TableOutputFormat.OUTPUT_TABLE, hbaseTable)

      val htable = MyHbaseUtil.getTable(hbaseConf, hbaseTable)
      val put = new Put(Bytes.toBytes(x._1))
      put.add(Bytes.toBytes("cf"), Bytes.toBytes("all"), Bytes.toBytes(x._2.toString))
      htable.put(put)

      println(x)
      Thread.sleep(100)
    }

  }


}
