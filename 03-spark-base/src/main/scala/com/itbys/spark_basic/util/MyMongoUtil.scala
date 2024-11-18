package com.itbys.spark_basic.util

import com.mongodb.spark.MongoSpark
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.bson.Document


/**
  * Author xx
  * Date 2023/12/30
  * Desc
  */
object MyMongoUtil {

  case class DataModel(id: String, advantage: String, city: String, education: String, industry: String,
                       label: String, position_detail: String, position_name: String, salary: String,
                       size: String, stage: String, work_year: String)

  def main(args: Array[String]): Unit = {

    //定义环境、连接
    val conf: SparkConf = new SparkConf().setAppName(getClass.getName).setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder().config(conf)
      .config("spark.mongodb.output.uri", "mongodb://hdp/test.employ_info")
      .getOrCreate()

    //转化实体类
    val modelRDD: RDD[DataModel] = spark.sparkContext.textFile("input/part-r-00000")
      .filter(_.length > 0)
      .map { x =>
        val strings: Array[String] = x.split("\t")
        DataModel(strings(0), strings(1), strings(2), strings(3), strings(4), strings(5),
          strings(6), strings(7), strings(8), strings(9), strings(10), strings(11))
      }
    modelRDD.foreach(println(_))

    //转化document对象
    val docRDD: RDD[Document] = modelRDD.map(
      x => {
        val document = new Document()
        document.append("id", x.id).append("advantage", x.advantage).append("city", x.city).append("education", x.education).append("industry", x.industry)
          .append("label", x.label).append("position_detail", x.position_detail).append("position_name", x.position_name).append("salary", x.salary)
          .append("size", x.size).append("stage", x.stage).append("work_year", x.work_year)
        document
      }
    )

    MongoSpark.save(docRDD)

  }

}
