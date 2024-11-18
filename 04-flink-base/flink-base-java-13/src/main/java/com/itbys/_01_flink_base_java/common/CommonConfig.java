package com.itbys._01_flink_base_java.common;

/**
 * Author chenj
 * Date 2021/9/2
 * Desc 配置类
 */
public class CommonConfig {

    //kafka
    public static final String KAFKA_SERVER = "hdp101:9092,hdp102:9092,hdp103:9092";
    public static final String DEFAULT_TOPIC = "default_topic";

    //主机: 10.68.9.30
    //账户：-uroot -ptcl@admin
    //库表：db_alerts.alerts
    //mysql
    public static final String MYSQL_HOST = "10.68.9.30";
    public static final Integer MYSQL_PORT = 3306;
    public static final String MYSQL_USER = "root";
    public static final String MYSQL_PASSED = "tcl@admin";
    public static final String MYSQL_DB = "db_alerts";
    public static final String MYSQL_TBL = "alerts";


}
