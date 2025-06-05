package com.sc.util

import scala.xml.{XML, Node}
import java.io.{File, FileWriter}
import java.util.{LinkedHashMap => JLinkedMap, List => JList, ArrayList => JArrayList}
import scala.collection.JavaConverters._
import org.yaml.snakeyaml.{Yaml, DumperOptions}

object XmlToYamlConverter {

  def nodeToOrderedMap(node: Node): Any = {
    val map = new JLinkedMap[String, Any]()

    // 按顺序添加属性
    node.attributes.foreach { attr =>
      map.put(attr.key, attr.value.text)
    }

    val children = node.child.filter(_.isInstanceOf[scala.xml.Elem])
    val text = node.child.filter(_.isAtom).map(_.text.trim).mkString.trim
    val hasChildren = children.nonEmpty
    val hasText = text.nonEmpty

    // 按顺序添加子元素
    for (child <- node.child) {
      child match {
        case e: scala.xml.Elem =>
          val childValue = nodeToOrderedMap(e)
          if (map.containsKey(e.label)) {
            // 已有同名字段 -> 转为列表
            map.get(e.label) match {
              case list: JList[Any] =>
                list.add(childValue)
              case existing =>
                val newList = new JArrayList[Any]()
                newList.add(existing)
                newList.add(childValue)
                map.put(e.label, newList)
            }
          } else {
            map.put(e.label, childValue)
          }

        case _ => // 其他忽略（文本已处理）
      }
    }

    // 如果只有文本
    if (!hasChildren && map.isEmpty && hasText) {
      return text
    }

    // 如果同时有文本内容
    if (hasText && (hasChildren || !map.isEmpty)) {
      map.put("_value", text)
    }

    map
  }

  def convertXmlToYaml(xmlPath: String, yamlPath: String): Unit = {
    val xml = XML.loadFile(xmlPath)
    val root = new JLinkedMap[String, Any]()
    root.put(xml.label, nodeToOrderedMap(xml))

    val options = new DumperOptions()
    options.setPrettyFlow(true)
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK)
    val yaml = new Yaml(options)

    val writer = new FileWriter(new File(yamlPath))
    yaml.dump(root, writer)
    writer.close()
  }

  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
      println("用法: XmlToYamlConverter <输入.xml> <输出.yaml>")
      System.exit(1)
    }

    val xmlFile = args(0)
    val yamlFile = args(1)
    convertXmlToYaml(xmlFile, yamlFile)
    println(s"转换完成: $xmlFile -> $yamlFile")
  }
}

