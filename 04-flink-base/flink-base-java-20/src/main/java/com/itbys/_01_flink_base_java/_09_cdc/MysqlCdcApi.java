package com.itbys._01_flink_base_java._09_cdc;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Author Ari
 * Date 2024/6/9
 * Desc
 */
public class MysqlCdcApi {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.execute();

    }

}
