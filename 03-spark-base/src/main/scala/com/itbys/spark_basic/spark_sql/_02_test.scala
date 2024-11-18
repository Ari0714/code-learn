package com.itbys.spark_basic.spark_sql

import com.itbys.spark_basic.bean.UserVisitAction
import org.apache.spark.SparkConf
import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object _02_test {

  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName(getClass.getSimpleName).setMaster("local[*]")
    val spark: SparkSession = SparkSession.builder().config(conf).enableHiveSupport().getOrCreate()
    import spark.implicits._

    val uvFrame: DataFrame = spark.sql("select * from spark_sql_test.user_visit_action")
    val proFrame: DataFrame = spark.sql("select * from spark_sql_test.product_info")
    val cidFrame: DataFrame = spark.sql("select * from spark_sql_test.city_info")

    uvFrame.createOrReplaceTempView("user_visit_action")
    proFrame.createOrReplaceTempView("product_info")
    cidFrame.createOrReplaceTempView("city_info")

    //    spark.udf.register("cityMark",functions.udf(new MyAggregator()))

    spark.sql(
      """
        |SELECT * from (
        |	SELECT *, rank() over(PARTITION by area order by cc desc) rk
        |		from (
        |			SELECT ci.area, pr.product_name, count(*) cc,
        |     cityMark(city_name) mark
        |			from user_visit_action uv
        |			join city_info ci on uv.city_id = ci.city_id
        |			join product_info pr on uv.click_product_id = pr.product_id
        |			where uv.click_product_id != -1
        |			group by ci.area, pr.product_name
        |		) t
        |	) t2
        |where rk < 4
      """.stripMargin)
      .show()

  }


  // TODO: 3.自定义UDAF函数
  //in 城市信息
  //buff : Buffer[Long,mutable.Map[String,Long]]    总点击，（城市，点击）
  //out 备注信息

  case class Buffer(var total: Long, var cityMap: mutable.Map[String, Long])

  class MyAggregator extends Aggregator[String, Buffer, String] {

    //初始化
    override def zero: Buffer = Buffer(0L, mutable.Map[String, Long]())

    //更新缓冲区数据
    override def reduce(buffer: Buffer, city: String): Buffer = {
      val newCnt: Long = buffer.cityMap.getOrElse(city, 0L) + 1
      buffer.cityMap.update(city, newCnt)
      buffer
    }

    //两个map合并u
    override def merge(buffer1: Buffer, buffer2: Buffer): Buffer = {

      buffer1.total += buffer2.total

      val map1: mutable.Map[String, Long] = buffer1.cityMap
      val map2: mutable.Map[String, Long] = buffer2.cityMap

      buffer1.cityMap = map1.foldLeft(map2) {
        case (map, (city, count)) =>
          val newCnt: Long = map.getOrElse(city, 0L) + count
          map.update(city, newCnt)
          map
      }
      buffer1
    }

    override def finish(reduction: Buffer): String = {

      val listBuffer = new ListBuffer[String]

      var totalcnt: Long = reduction.total
      if (totalcnt == 0) {
        totalcnt = 1L
      }

      val cityMap: mutable.Map[String, Long] = reduction.cityMap

      val sortList: List[(String, Long)] = cityMap.toList.sortWith((a, b) => a._2 > b._2).take(2)

      sortList.foreach { case (city, cnt) =>
        val ratio: Long = cnt * 100 / totalcnt
        listBuffer.append(city + ":" + ratio)
      }
      listBuffer.mkString(", ")
    }

    override def bufferEncoder: Encoder[Buffer] = Encoders.product

    override def outputEncoder: Encoder[String] = Encoders.STRING
  }


}
