package com.sc.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import org.yaml.snakeyaml.Yaml

import scala.collection.JavaConverters._
import scala.io.Source
import scala.util.Try
import scala.io.Source
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import org.yaml.snakeyaml.Yaml
import scala.collection.JavaConverters._
import scala.util.Try


/**
  * Author Ari
  * Date 2025/6/27
  * Desc 
  */
object DataTypeVAlidation4 {

  case class ColumnDetail(col_name: String, data_type: String, notnull: Boolean)

  def main(args: Array[String]): Unit = {
    val yamlFile = "00-tcl/input/sc/02-data-validation/table_config.yaml"
    val csvFile = "00-tcl/input/sc/02-data-validation/table_data.csv"
    val primaryKey = "id"

    val schema = parseYaml(yamlFile)
    val csvLines = Source.fromFile(csvFile).getLines().toList
    val headers = csvLines.head.split(",").map(_.trim)
    val data = csvLines.tail.map(_.split(",").map(_.trim))
    val colIndex = headers.zipWithIndex.toMap

    println("%-40s | %-40s".format("数据行", "问题描述"))
    println("-" * 90)

    data.foreach { row =>
      val baseProblems = if (row.length != schema.length) {
        List(s"数据列数(${row.length})与YAML字段数(${schema.length})不一致")
      } else {
        validateRow(row, schema, colIndex, primaryKey)
      }
      if (baseProblems.nonEmpty) {
        println("%-40s | %-40s".format(row.mkString(","), baseProblems.mkString("； ")))
      }
    }
  }

  def parseYaml(path: String): List[ColumnDetail] = {
    val yaml = new Yaml()
    val input = Source.fromFile(path).mkString
    val root = yaml.load(input).asInstanceOf[java.util.Map[String, java.util.List[java.util.Map[String, Any]]]]
    val detailList = root.get("col_details").asScala.toList
    detailList.map { entry =>
      val m = entry.asScala
      ColumnDetail(
        col_name = m("col_name").toString.trim,
        data_type = m("data_type").toString.toLowerCase.trim,
        notnull = m.get("notnull").map(_.toString.toBoolean).getOrElse(false)
      )
    }
  }

  def validateRow(row: Array[String], schema: List[ColumnDetail], colIndex: Map[String, Int], primaryKey: String): List[String] = {
    var problems = List[String]()

    val pkValue = getValue(row, colIndex.get(primaryKey))
    if (pkValue.isEmpty) problems ::= s"主键[$primaryKey]为空"

    schema.foreach { col =>
      val value = getValue(row, colIndex.get(col.col_name))
      val dtype = col.data_type

      if (col.notnull && value.isEmpty)
        problems ::= s"字段[${col.col_name}]不能为空"

      if (dtype.startsWith("varchar") && getLength(dtype).exists(value.length > _))
        problems ::= s"字段[${col.col_name}]超过最大长度"
      if (dtype.startsWith("char") && getLength(dtype).exists(value.length == _))
        problems ::= s"字段[${col.col_name}]不符合长度"

      if (dtype.contains("int") && value.nonEmpty && !Try(value.toInt).isSuccess)
        problems ::= s"字段[${col.col_name}]应为整数"

      if (dtype.contains("date") && !dtype.contains("timestamp") && value.nonEmpty && !isValidFormat(value, "yyyy-MM-dd"))
        problems ::= s"字段[${col.col_name}]应为yyyy-MM-dd格式"

      if (dtype.contains("timestamp") && value.nonEmpty && !isValidFormat(value, "yyyy-MM-dd HH:mm:ss"))
        problems ::= s"字段[${col.col_name}]应为yyyy-MM-dd HH:mm:ss格式"
    }

    problems.reverse
  }

  def getValue(row: Array[String], idxOpt: Option[Int]): String = {
    idxOpt match {
      case Some(i) if i < row.length => row(i).replace("'", "").trim
      case _ => ""
    }
  }

  def getLength(dtype: String): Option[Int] = {
    val pattern = "\\((\\d+)\\)".r
    pattern.findFirstMatchIn(dtype).map(_.group(1).toInt)
  }

  def isValidFormat(value: String, pattern: String): Boolean = {
    Try {
      val formatter = DateTimeFormatter.ofPattern(pattern)
      if (pattern.contains("HH")) LocalDateTime.parse(value, formatter)
      else formatter.parse(value)
    }.isSuccess
  }


}
