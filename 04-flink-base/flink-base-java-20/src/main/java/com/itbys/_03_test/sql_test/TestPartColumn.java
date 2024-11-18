package com.itbys._03_test.sql_test;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * Author chenjie
 * Date 2024/6/27
 * Desc
 */
public class TestPartColumn {

    public static void main(String[] args) {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        //设置tableEnv的参数
        Configuration configuration = tableEnv.getConfig().getConfiguration();
        configuration.setString("table.exec.sink.upsert-materialize", "none");
//        configuration.setString("table.exec.state.ttl", "30 s");

        tableEnv.executeSql("CREATE TABLE sourceTable (\n" +
                "                 uuid varchar(20),\n" +
                "                  name varchar(10),\n" +
                "                  age varchar(10),\n" +
                "                  ts varchar(20)\n" +
                "                ) WITH (\n" +
                "                  'connector' = 'datagen',\n" +
                "                  'rows-per-second' = '1'\n" +
                "                );");

        tableEnv.executeSql("CREATE TABLE sourceTable (\n" +
                "                 uuid varchar(20),\n" +
                "                  name varchar(10),\n" +
                "                  age varchar(10),\n" +
                "                  ts varchar(20)\n" +
                "                ) WITH (\n" +
                "                  'connector' = 'datagen',\n" +
                "                  'rows-per-second' = '1'\n" +
                "                );");


        tableEnv.executeSql("INSERT INTO pg_sink_table\n" +
                "SELECT * from mysql_source_table");



    }

}
