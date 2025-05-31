package com.atguigu.iot.bean

case class SensorReading(
                          sensorId: String,
                          timestamp: Long,
                          temperature: Double,
                          humidity: Double
                        )


case class AlertRule(
                      sensorId: String,
                      maxTemp: Double,
                      maxHumidity: Double
                    )

case class Alert(
                  sensorId: String,
                  timestamp: Long,
                  temp: Double,
                  hum: Double,
                  msg: String
                )


