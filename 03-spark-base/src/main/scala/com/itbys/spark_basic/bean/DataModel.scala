package com.itbys.spark_basic.bean

case class AdsLog(
                   timestamp: Long,
                   area: String,
                   city: String,
                   userid: String,
                   adid: String
                 )


case class AdUserClickCount(date: String,
                            userid: String,
                            adid: String,
                            clickCount: Long
                           )

case class CityInfo(
                     city_id: Long,
                     city_name: String,
                     area: String
                   )


case class AdBlacklist(userId: Long)

case class AdUserClickCountt(count: Long)