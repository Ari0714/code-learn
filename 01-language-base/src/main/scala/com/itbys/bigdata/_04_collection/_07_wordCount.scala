package com.itbys.bigdata._04_collection

/**
  * Author xx
  * Date 2023/3/14
  * Desc
  */
object _07_wordCount {

  def main(args: Array[String]): Unit = {

    val tuples = List(("hello world", 4), ("hello scala", 6), ("world peace", 8), ("scala NB", 100))

    val tuples1 = tuples.flatMap(x => {
      val strings = x._1.split(" ")
      strings.map(strings => (strings, x._2))
    })

    val stringToTuples = tuples1.groupBy(x => x._1)
    //    println(stringToTuples)


    val stringToInt = stringToTuples.map(x => {
      val ints = x._2.map(xx => xx._2)
      (x._1, ints.sum)
    })

    //mapvalues
    val stringToInt1 = stringToTuples.mapValues(datas => datas.map(x => x._2).sum)

    println(stringToInt1.toList.sortWith((x, y) => {
      x._2 > y._2
    }).take(3))


  }

}
