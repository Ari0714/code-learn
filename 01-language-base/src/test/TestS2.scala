import java.io.IOException

import scala.io.Source

object TestS2 {

  def main(args: Array[String]): Unit = {
    // 获取文件路径
    val filePath = "01-language-base/input/datax.txt"

    // 使用 Source 对象读取文件内容
    try {
      val source = Source.fromFile(filePath)

      // 逐行读取文件并打印到控制台
      for (line <- source.getLines()) {
        println(line)
      }

      // 关闭 Source 对象以释放资源
      source.close()
    } catch {
      // 捕获并处理可能发生的异常，如文件不存在或读取错误
      case e: IOException => println(s"Error reading file: ${e.getMessage}")
    }
  }


}
