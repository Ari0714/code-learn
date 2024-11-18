object TestS {

    def main(args: Array[String]): Unit = {
      var arr1: Array[Int] = Array(5,9,3,7,6,8)
      var arr2: Array[Int] = Array(1,4,2)
      val array = paixu(arr1,arr2)

      println(array.mkString(","))

    }

    def paixu(arr1: Array[Int],arr2: Array[Int]): Array[Int] = {
      // 合并数组
      val combinedArray = arr1 ++ arr2
      // 对合并后的数组进行升序排序
      val sortedArray = combinedArray.sorted
      // 打印排序后的数组
      sortedArray
    }


}
