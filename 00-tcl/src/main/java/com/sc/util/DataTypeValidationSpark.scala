package com.sc.util

import java.text.SimpleDateFormat

import org.yaml.snakeyaml.Yaml

import scala.collection.JavaConverters._
import scala.io.Source
import org.apache.spark.sql.{Row, SparkSession}
import org.yaml.snakeyaml.Yaml

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.collection.JavaConverters._
import scala.util.Try
import scala.io.Source


/**
  * Author Ari
  * Date 2025/6/25
  * Desc 
  */
object DataTypeValidationSpark {

  case class ColumnDetail(col_name: String, data_type: String, notnull: Boolean)

  def main(args: Array[String]): Unit = {
    val yamlFile = "00-tcl/input/sc/02-data-validation/table_config.yaml"
    val csvFile = "00-tcl/input/sc/02-data-validation/table_data.csv"
    val primaryKey = "id"

    val spark = SparkSession.builder()
      .appName("DataValidation")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    // Load schema definition from YAML
    val schema = parseYaml(yamlFile)

    // Read CSV as DataFrame
    val df = spark.read
      .option("header", "true")
      .option("inferSchema", "false")
      .option("delimiter", ",")
      .csv(csvFile)

    df.show()

    val headers = df.columns
    val colIndex = headers.zipWithIndex.toMap

    println(f"${"Row Data"}%-40s | ${"Validation Issues"}%-40s")
    println("-" * 90)

    df.collect().foreach { row =>
      println(row.size)
      println(schema.size)
      val problems =
        if (row.size != schema.length)
          List(s"Column count (${row.size}) does not match schema (${schema.length})")
        else
          validateRow(row, schema, colIndex, primaryKey)

      if (problems.nonEmpty) {
        println(f"${row.mkString(",")}%40s | ${problems.mkString("; ")}")
      }
    }

    spark.stop()
  }

  // Parse schema definition from YAML
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

  // Validate a single row against schema
  def validateRow(row: Row, schema: List[ColumnDetail], colIndex: Map[String, Int], primaryKey: String): List[String] = {
    var problems = List[String]()

    val pkValue = getValue(row, colIndex.get(primaryKey))
    if (pkValue.isEmpty) problems ::= s"Primary key [$primaryKey] is empty"

    schema.foreach { col =>
      val value = getValue(row, colIndex.get(col.col_name))
      val dtype = col.data_type

      if (col.notnull && value.isEmpty)
        problems ::= s"Field [${col.col_name}] should not be null"

      if (dtype.startsWith("varchar") && getLength(dtype).exists(value.length > _))
        problems ::= s"Field [${col.col_name}] exceeds max length"
      if (dtype.startsWith("char") && getLength(dtype).exists(value.length != _))
        problems ::= s"Field [${col.col_name}] does not match fixed length"

      if (dtype.contains("int") && value.nonEmpty && !Try(value.toInt).isSuccess)
        problems ::= s"Field [${col.col_name}] should be an integer"

      if (dtype.contains("date") && !dtype.contains("timestamp") && value.nonEmpty && !isValidFormat(value, "yyyy-MM-dd"))
        problems ::= s"Field [${col.col_name}] should be in yyyy-MM-dd format"

      if (dtype.contains("timestamp") && value.nonEmpty && !isValidFormat(value, "yyyy-MM-dd HH:mm:ss"))
        problems ::= s"Field [${col.col_name}] should be in yyyy-MM-dd HH:mm:ss format"
    }

    problems.reverse
  }

  // Get field value from Row by index
  def getValue(row: Row, idxOpt: Option[Int]): String = {
    idxOpt match {
      case Some(i) if i < row.length && !row.isNullAt(i) =>
        row.getString(i).replace("'", "").trim
      case _ => ""
    }
  }

  // Extract length limit from type definition (e.g., varchar(10))
  def getLength(dtype: String): Option[Int] = {
    val pattern = "\\((\\d+)\\)".r
    pattern.findFirstMatchIn(dtype).map(_.group(1).toInt)
  }

  // Check whether a string matches a given datetime format
  def isValidFormat(value: String, pattern: String): Boolean = {
    Try {
      val formatter = DateTimeFormatter.ofPattern(pattern)
      if (pattern.contains("HH")) LocalDateTime.parse(value, formatter)
      else formatter.parse(value)
    }.isSuccess
  }


}
