package com.itbys.spark_basic.bean

/**
  * Author xx
  * Date 2022/10/2
  * Desc 
  */
case class CategoryCountInfo(
                              var categoryId: String, //品类id
                              var clickCount: Long, //点击次数
                              var orderCount: Long, //订单次数
                              var payCount: Long //支付次数
                            )
