package com.itbys.spark_basic.util

import java.io.InputStreamReader
import java.util.Properties


/**
  * Author xx
  * Date 2022/2/3
  * Desc 读取配置文件
  */
object MyPropertiesUtil {

  def main(args: Array[String]): Unit = {
    val properties: Properties = MyPropertiesUtil.load("03-spark-base/input/config.properties")
    println(properties.getProperty("jdbc.url"))
  }


  /**
    * @desc 读取配置文件
    * @param: fileName
    * @return: java.com.tcl.com.itbys.util.Properties
    */
  def load(fileName: String): Properties = {
    val properties = new Properties
    properties.load(new InputStreamReader(Thread.currentThread().getContextClassLoader.getResourceAsStream(fileName), "UTF-8"))
    properties
  }


}
