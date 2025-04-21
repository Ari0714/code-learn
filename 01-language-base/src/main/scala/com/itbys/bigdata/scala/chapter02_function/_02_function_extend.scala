package com.itbys.bigdata.scala.chapter02_function

/**
  * Author xx
  * Date 2023/3/14
  * Desc
  */
object _02_function_extend {

  def main(args: Array[String]): Unit = {

    text01("coco")

    text02(sex = "female")

    demo01()

    test03("aaa", "bbb", "ccc")

    val i = test04(5)(6)
    println(i)

    println(test05(5)(6)(7))

  }

  //高阶函数 , 柯里化
  def test05(a: Int) = (b: Int) => (c: Int) => a * b * c

  //高阶函数 , 柯里化
  def test04(a: Int) = (b: Int) => a * b

  //WrappedArray(aaa, bbb, ccc) ,不传时List
  def test03(name: String*): Unit = {
    println(name)
  }

  //执行print，注意返回结果和打印
  def demo01() {
    def f1 = "venassa"

    println(f1)
  }

  def text02(name: String = "coco", sex: String): Unit = {
    println(name + "---" + sex)
  }


  //默认参数,只能是第一个空缺
  def text01(name: String, sex: String = "man"): Unit = {
    println(name + "---" + sex)
  }


}
