package com.itbys.spark_basic.util

import java.sql._
import java.util.Properties

import com.alibaba.fastjson.JSONObject
import com.itbys.spark_basic.common.CommonConfig
import org.apache.kafka.common.TopicPartition

import scala.collection.mutable.ListBuffer


/**
  * Author: Felix
  * Date: 2020/11/2
  * Desc: 从MySQL中读取偏移量的工具类
  */
object OffsetManagerM {


  /**
    * @desc 从Mysql中读取偏移量
    * @param: topic
    * @param: consumerGroupId
    * @return: scala.collection.immutable.Map<org.apache.kafka.common.TopicPartition,java.lang.Object>
    */
  def getOffset(topic: String, consumerGroupId: String) = {
    val sql = " select groupid,topic,untiloffset,partition from offset_manager " +
      " where topic='" + topic + "' and groupid='" + consumerGroupId + "'"

    val jsonObjList: List[JSONObject] = queryList(sql)

    val topicPartitionList: List[(TopicPartition, Long)] = jsonObjList.map {
      jsonObj => {
        val topicPartition: TopicPartition = new TopicPartition(topic, jsonObj.getIntValue("partition_id"))
        val offset: Long = jsonObj.getLongValue("topic_offset")
        (topicPartition, offset)
      }
    }
    val topicPartitionMap: Map[TopicPartition, Long] = topicPartitionList.toMap
    topicPartitionMap
  }


  /**
    * @desc 获取mysql数据 -》 json格式返回
    * @param: sql
    * @return: scala.collection.immutable.List<com.alibaba.fastjson.JSONObject>
    */
  def queryList(sql: String) = {

    Class.forName("com.mysql.jdbc.Driver")
    val resultList: ListBuffer[JSONObject] = new ListBuffer[JSONObject]()
    val conn: Connection = DriverManager.getConnection(CommonConfig.MYSQL_URL, CommonConfig.MYSQL_USER, CommonConfig.MYSQL_PASSWORD)

    val statement: PreparedStatement = conn.prepareStatement(sql)
    val resultSet: ResultSet = statement.executeQuery()
    val metaData: ResultSetMetaData = statement.getMetaData

    while (resultSet.next()) {
      val rowDate = new JSONObject()
      for (i <- 1 to metaData.getColumnCount) {
        rowDate.put(metaData.getColumnName(i), resultSet.getObject(i))
      }
      resultList += rowDate
    }

    resultSet.close()
    statement.close()
    conn.close()

    resultList.toList

  }

}
