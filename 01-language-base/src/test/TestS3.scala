import java.io.IOException

import scala.io.Source
import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import java.io.IOException

object TestS3 {

  def main(args: Array[String]): Unit = {

    // 获取文件路径和要写入的文字
    val filePath = "01-language-base/input/scala_output.txt"
    val textToWrite = "abc,aaa,qqq"

    // 使用 try-with-resources 语句确保 BufferedWriter 在使用后被正确关闭
    val writer = Files.newBufferedWriter(Paths.get(filePath), StandardCharsets.UTF_8)
    // 将文字写入文件
    writer.write(textToWrite)
    writer.close()
    println(s"Successfully wrote text to file: $filePath")

  }


}
