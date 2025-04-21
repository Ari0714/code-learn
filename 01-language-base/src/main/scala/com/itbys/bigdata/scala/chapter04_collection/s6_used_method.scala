package com.itbys.bigdata.scala.chapter04_collection

/**
  * Author xx
  * Date 2023/3/14
  * Desc
  */
object s6_used_method {

  def main(args: Array[String]): Unit = {

    val list1 = List(19, 21, 35, 4, 5, 8, 99, 76, 19, 5, 5, 5, 5, 5, 8888, 990, 6, 6, 6, 6, 5, 5, 324, 23, 4, 234, 234)

    //最大 小  和  积
    println(list1.max)
    println(list1.min)
    println(list1.sum)
    println(list1.product)


    //反转
    println(list1.reverse)

    //分组
    val list2 = list1.groupBy(x => x)
    list2.foreach(x => println(x._1 + "+" + x._2))


    val list3 = List("red", "yellow", "green", "grey", "red", "pink", "pink")
    val list4 = list3.groupBy(x => x)
    list4.foreach(x => println(x._1 + "=" + x._2))

    //根据首字母
    println()
    val list5 = list3.groupBy(x => x.substring(0, 1))
    list5.foreach(x => println(x._1 + "=" + x._2))


    //sortby(升序) sortwith
    //    val list6: List[Int] = tuples.sortBy(_,false)
    //    println(list6)

    val list7 = list1.sortWith((x, y) => {
      x > y
    })
    println(list7)

    val list8 = list1.sortWith((x, y) => {
      x < y
    })
    println(list8)

    val list9_1 = List(11, 45, 86, 34, 254, 86, 356, 224, 999, 5533)
    val list9 = list3.sortWith((x, y) => {
      x.substring(1) > y.substring(1)
    })
    println(list9)

    //迭代
    for (elem <- list1.iterator) {
      println(elem)
    }


    //map映射
    val tuples1 = list1.map(x => (x, 1))
    val tuples2 = tuples1.groupBy(x => x._1)
    val intToInt = tuples2.map(x => (x._1, x._2.size))
    intToInt.foreach(println)

    //demo01 worldCount 降序
    val list10 = List("aa", "bb", "cc", "rr", "aa", "ff", "ff", "ee", "bb", "bb")
    val stringToStrings = list10.groupBy(x => x)
    val stringToInt = stringToStrings.map(x => (x._1, x._2.size))
    val tuples = stringToInt.toList.sortWith((x, y) => {
      x._2 > y._2
    })
    println(tuples)


    //flatmap 扁平化 整体拆成
    val strings = List("aa bb nn", "gg bb dd", "aa dd gg", "hh ff ss", "aa aa aa")
    val tuples3 = strings.flatMap(x => x.split(" ")).groupBy(x => x).map(x => (x._1, x._2.size)).toList.sortWith((x, y) => {
      x._2 > y._2
    })

    println(tuples3)


    //filter 过略
    val ints = List(1, 2, 3, 4, 5)
    println(ints.filter(x => x % 2 == 0))


    //拉链  zip  多的不连接
    val list11 = List(1, 3, 5, 7, 9)
    val list12 = List(2, 4, 6, 8)
    println(list11.zip(list12))

    //交并集
    val list13 = List(1, 3, 5, 7, 9)
    val list14 = List(1, 4, 6, 8, 10)

    println(list13.union(list14))

    println(list13.intersect(list14))

    println(list13.diff(list14))
    println(list14.diff(list11))


  }

}
