package com.itbys.spark_basic.java.common;

/**
 * Author Ari
 * Date 2022/4/11
 * Desc
 */
public class CommonConfig {

    /**
     * pg库地址
     */
    //qv_statistics
    public static String qv_statistics_url = "jdbc:postgresql://1.15.109.74:22505/qv_statistics";
    public static String qv_statistics_user = "bigdata_crud";
    public static String qv_statistics_passwd = "WS2ws91d26649fe18f";


    //clickhouse
    public static String clickhouse_driver = "ru.yandex.clickhouse.ClickHouseDriver";
    public static String clickhouse_url = "jdbc:clickhouse://172.17.43.17:8123/default";
    public static String clickhouse_user = "default";
    public static String clickhouse_passwd = "wshoto666";


    //kafka地址
    public static String kafka_broker_list = "172.17.0.48:9092";

    //redis地址
    public static String redis_ip = "172.17.0.48";
    public static int redis_port = 6379;

}
