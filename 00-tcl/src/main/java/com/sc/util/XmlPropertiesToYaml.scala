package com.sc.util


import scala.xml.{XML, Node}
import java.io.{File, FileWriter}
import java.util.{LinkedHashMap => JLinkedMap}
import org.yaml.snakeyaml.{Yaml, DumperOptions}

object XmlPropertiesToYaml {

  def extractNameValuePairs(xml: Node): JLinkedMap[String, String] = {
    val map = new JLinkedMap[String, String]()
    val properties = xml \\ "property"
    for (prop <- properties) {
      val name = (prop \ "name").text.trim
      val value = (prop \ "value").text.trim
      if (name.nonEmpty) {
        map.put(name, value)
      }
    }
    map
  }

  def convert(xmlPath: String, yamlPath: String): Unit = {
    val xml = XML.loadFile(xmlPath)
    val map = extractNameValuePairs(xml)

    val options = new DumperOptions()
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK)
    options.setPrettyFlow(true)
    val yaml = new Yaml(options)

    val writer = new FileWriter(new File(yamlPath))
    yaml.dump(map, writer)
    writer.close()
  }

  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      println("用法: XmlPropertiesToYaml <输入.xml> <输出.yaml>")
      System.exit(1)
    }

    val xmlFile = args(0)
    val yamlFile = args(1)
    convert(xmlFile, yamlFile)
    println(s"转换完成: $xmlFile -> $yamlFile")
  }
}



