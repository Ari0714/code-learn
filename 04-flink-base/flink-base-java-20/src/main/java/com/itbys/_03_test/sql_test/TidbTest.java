package com.itbys._03_test.sql_test;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * Author chenjie
 * Date 2023/11/28
 * Desc 测试tidb的端到端方案
 *      1、问题：changelogNomalize大
 */
public class TidbTest {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        //设置tableEnv的参数
        Configuration configuration = tableEnv.getConfig().getConfiguration();
        configuration.setString("table.exec.sink.upsert-materialize", "none");
//        configuration.setString("table.exec.state.ttl", "30 s");


        //1.2 设置并行度
        env.setParallelism(1);

        //TODO 2.检查点相关设置
        //2.1 开启检查点  (开启检查点后自动提交偏移量到hdfs和kafka，auto.commit=true)
//        env.enableCheckpointing(5000L, CheckpointingMode.EXACTLY_ONCE);
////        2.2 设置检查点超时时间
//        env.getCheckpointConfig().setCheckpointTimeout(60000);
////        2.3 设置重启策略
//        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3,3000L));
////        2.4 设置job取消后，检查点是否保留
//        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
////        2.5 设置状态后端   内存|文件系统|RocksDB
//        env.setStateBackend(new FsStateBackend("hdfs://hdp:8020/ck/gmall"));
////        2.6 指定操作HDFS的用户
//        System.setProperty("HADOOP_USER_NAME","root");


        tableEnv.executeSql("CREATE TABLE orders (\n" +
                "     order_id INT,\n" +
                "     order_date TIMESTAMP(3),\n" +
                "     customer_name STRING,\n" +
                "     price DECIMAL(10, 5),\n" +
                "     product_id INT,\n" +
                "     order_status BOOLEAN,\n" +
                "     PRIMARY KEY(order_id) NOT ENFORCED\n" +
                "     ) WITH (\n" +
                "     'connector' = 'tidb-cdc',\n" +
                "     'tikv.grpc.timeout_in_ms' = '20000', \n" +
                "     'pd-addresses' = 'localhost:2379',\n" +
                "     'database-name' = 'test',\n" +
                "     'table-name' = 'orders'\n" +
                ");");


        tableEnv.executeSql("CREATE TABLE tidb_kafka (\n" +
                "order_id INT,\n" +
                "order_date TIMESTAMP(3),\n" +
                "customer_name varchar(20),\n" +
                "price DECIMAL(10, 5),\n" +
                "product_id INT,\n" +
                "order_status BOOLEAN,\n" +
                "PRIMARY KEY (order_id) NOT ENFORCED\n" +
                ") WITH (\n" +
                "  'connector' = 'upsert-kafka',\n" +
                "  'topic' = 'tidb_kafka',\n" +
                "  'properties.bootstrap.servers' = 'hdp:9092',\n" +
                "  'key.format' = 'json',\n" +
                "  'value.format' = 'json'\n" +
                ");");


        tableEnv.executeSql("INSERT INTO tidb_kafka\n" +
                "SELECT order_id, order_date, customer_name, price, product_id, order_status from orders");




//        String topic = "dwm_order_wide";
//        String groupId = "province_stats_app_group";
//
//        //创建表环境：1、指定字段名称和json的key相同才能解析。2、执行watermark
//        tableEnv.executeSql("create table order_wide ( " +
//                " province_id BIGINT, " +
//                " province_name STRING, " +
//                " province_area_code STRING, " +
//                " province_iso_code STRING, " +
//                " province_3166_2_code STRING, " +
//                " order_id STRING, " +
//                " split_total_amount DOUBLE, " +
//                " create_time STRING, " +
//                " rowtime as TO_TIMESTAMP(create_time), " +
//                " WATERMARK FOR rowtime AS rowtime - INTERVAL '3' SECOND " +
//                ") with ( " +
//                "  'connector' = 'kafka', " +
//                "  'topic' = '"+topic+"', " +
//                "  'properties.bootstrap.servers' = '"+ CommonConfig.KAFKA_SERVER +"', " +
//                "  'properties.group.id' = '"+groupId+"', " +
//                "  'scan.startup.mode' = 'earliest-offset', " +
//                "  'format' = 'json' " +
//                ")");
////        tableEnv.sqlQuery("select * from order_wide").execute().print();
//
//        //执行查询
//        Table table = tableEnv.sqlQuery("select  " +
//                " DATE_FORMAT(TUMBLE_START(rowtime,INTERVAL '10' SECOND), 'yyyy-MM-dd HH:mm:ss') as stt, " +
//                " DATE_FORMAT(TUMBLE_END(rowtime,INTERVAL '10' SECOND),'yyyy-MM-dd HH:mm:ss') as edt, " +
//                " province_id, province_name, province_area_code area_code, province_iso_code iso_code, province_3166_2_code iso_3166_2, " +
//                " sum(split_total_amount) order_amount, " +
//                " count(distinct order_id) order_count, " +
//                " UNIX_TIMESTAMP() * 1000 as ts " +
//                " from order_wide " +
//                " group by " +
//                " TUMBLE(rowtime,INTERVAL '10' SECOND), " +
//                " province_id, province_name, province_area_code, province_iso_code, province_3166_2_code");
//        table.execute().print();
        ;



        env.execute();

    }

}
