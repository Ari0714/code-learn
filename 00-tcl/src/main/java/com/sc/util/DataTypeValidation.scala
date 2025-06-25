package com.sc.util

import org.yaml.snakeyaml.Yaml
import scala.io.Source
import scala.collection.JavaConverters._
import com.github.tototoshi.csv._

import java.text.SimpleDateFormat

/**
  * Author Ari
  * Date 2025/6/25
  * Desc 
  */
object DataTypeValidation {

  case class ColumnDetail(col_name: String, data_type: String, notnull: Boolean = false)

  def parseSchema(yamlPath: String): List[ColumnDetail] = {
    val yaml = new Yaml()
    val input = Source.fromFile(yamlPath).mkString
    val parsed = yaml.load(input).asInstanceOf[java.util.Map[String, Object]]
    val colList = parsed.get("col_details").asInstanceOf[java.util.List[java.util.Map[String, Object]]]
    colList.asScala.map { item =>
      val name = item.get("col_name").toString
      val dtype = item.get("data_type").toString
      val notnull = Option(item.get("notnull")).exists(_.toString.toBoolean)
      ColumnDetail(name, dtype, notnull)
    }.toList
  }

  def checkPrimaryKeyNull(row: Map[String, String], primaryKey: String): Option[String] = {
    if (row.getOrElse(primaryKey, "").trim.isEmpty) Some("主键为空") else None
  }

  def checkNotNullViolation(row: Map[String, String], schema: List[ColumnDetail]): Seq[String] = {
    schema.filter(_.notnull).flatMap { col =>
      if (row.getOrElse(col.col_name, "").trim.isEmpty) Some(s"${col.col_name} 不能为空") else None
    }
  }

  def checkDataTypeMismatch(row: Map[String, String], schema: List[ColumnDetail]): Seq[String] = {
    schema.flatMap { col =>
      val v = row.getOrElse(col.col_name, "").trim
      if (v.nonEmpty && !isTypeMatch(v, col.data_type)) Some(s"${col.col_name} 类型错误") else None
    }
  }

  def isTypeMatch(value: String, dtype: String): Boolean = dtype match {
    case t if t.startsWith("varchar") || t.startsWith("char") => true
    case "int" => value.matches("-?\\d+")
    case "date" => try {
      new SimpleDateFormat("yyyy-MM-dd").parse(value); true
    } catch {
      case _: Exception => false
    }
    case _ => true
  }

  def checkDateFormat(row: Map[String, String], schema: List[ColumnDetail]): Seq[String] = {
    schema.filter(_.data_type == "date").flatMap { col =>
      val v = row.getOrElse(col.col_name, "").trim
      if (v.nonEmpty && !isValidDate(v)) Some(s"${col.col_name} 日期格式非法") else None
    }
  }

  def isValidDate(s: String): Boolean = {
    try {
      val df = new SimpleDateFormat("yyyy-MM-dd")
      df.setLenient(false)
      df.parse(s)
      true
    } catch {
      case _: Exception => false
    }
  }

  def checkMaxLength(row: Map[String, String], schema: List[ColumnDetail]): Seq[String] = {
    schema.flatMap { col =>
      val value = row.getOrElse(col.col_name, "")
      val m = "varchar\\((\\d+)\\)".r.findFirstMatchIn(col.data_type)
        .orElse("char\\((\\d+)\\)".r.findFirstMatchIn(col.data_type))
      m match {
        case Some(mm) =>
          val maxLen = mm.group(1).toInt
          if (value.length > maxLen) Some(s"${col.col_name} 长度超出(${value.length}/${maxLen})") else None
        case None => None
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val schema = parseSchema("00-tcl/input/sc/02-data-validation/table_config.yaml")
    val primaryKey = "id"

    val reader = CSVReader.open("00-tcl/input/sc/02-data-validation/table_data.csv")
    val rows = reader.allWithHeaders()
    reader.close()

    println("校验错误记录：")

    rows.zipWithIndex.foreach { case (row, idx) =>
      val issues =
        checkPrimaryKeyNull(row, primaryKey).toList ++
          checkNotNullViolation(row, schema) ++
          checkDataTypeMismatch(row, schema) ++
          checkDateFormat(row, schema) ++
          checkMaxLength(row, schema)

      if (issues.nonEmpty) {
        val line = schema.map(c => row.getOrElse(c.col_name, "")).mkString(",")
        println(s"第 ${idx + 2} 行: [$line] ⛔ ${issues.mkString("；")}")
      }
    }
  }


}
