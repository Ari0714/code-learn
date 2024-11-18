package com.itbys._03_test.common;

/**
 * Author chenj
 * Date 2021/9/2
 * Desc 配置类
 */
public class CommonConfig {

    //kafka
    public static final String KAFKA_SERVER = "hdp101:9092,hdp102:9092,hdp103:9092";
    public static final String DEFAULT_TOPIC = "default_topic";

    //phoneix
    public static final String HBASE_SCHEMA="GMALL2021_REALTIME";
    public static final String PHOENIX_SERVER="jdbc:phoenix:hdp101,hdp102,hdp103:2181";

    //redis
    public static final String REDIS_SERVER="hdp103";
    public static final Integer REDIS_PORT=6379;

    //clickhouse
    public static final String CLICKHOUSE_URL = "jdbc:clickhouse://hdp102:8123/default";



}
