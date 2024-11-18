package com.itbys._01_flink_base_java._09_cdc;

import com.itbys._01_flink_base_java.bean.AlertsEntity;
import com.itbys._01_flink_base_java.common.CommonConfig;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

/**
 * Author chenjie
 * Date 2024/6/9
 * Desc
 */
public class CdcTestSql {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.enableCheckpointing(3000L);
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        //CREATE TABLE `alerts` (
        //  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
        //  `name` varchar(255) NOT NULL COMMENT '告警的名称',
        //  `level` varchar(50) NOT NULL COMMENT '告警等级：警告(Warning)，一般(Average)，严重(High)',
        //  `description` text COMMENT '告警的描述',
        //  `trigger_condition` text COMMENT '触发告警的条件',
        //  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '告警时间',
        //  `updated_time` varchar(50) DEFAULT NULL COMMENT '恢复时间',
        //  `alert_status` varchar(50) DEFAULT '0' COMMENT '是否推送告警，0 未推送，1 已推送',
        //  PRIMARY KEY (`id`)
        //) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4
        String testCDCSource = "create table " + CommonConfig.MYSQL_TBL + " ( \n" +
                "id int, \n" +
                "name varchar(255)," +
                "level varchar(255)," +
                "description varchar(255)," +
                "trigger_condition varchar(255)," +
                "created_time timestamp," +
                "updated_time varchar(255)," +
                "alert_status varchar(255)," +
                "primary key(id) not ENFORCED \n" +
                ") with ( \n" +
                "'connector' = 'mysql-cdc', \n" +
                "'hostname' = '" + CommonConfig.MYSQL_HOST + "', \n" +
                "'port' = '" + CommonConfig.MYSQL_PORT + "', \n" +
                "'username' = '" + CommonConfig.MYSQL_USER + "', \n" +
                "'password' = '" + CommonConfig.MYSQL_PASSED + "', \n" +
                "'database-name' = '" + CommonConfig.MYSQL_DB + "', \n" +
                "'table-name' = '" + CommonConfig.MYSQL_TBL + "' \n" +
                ")";

        System.out.println(testCDCSource);
//        String testCDCSource = "create table " + CommonConfig.MYSQL_TBL + " ( \n" +
//                "id int, \n" +
//                "name varchar(255)," +
//                "level varchar(255)," +
//                "description varchar(255)," +
//                "trigger_condition varchar(255)," +
//                "created_time varchar(255)," +
//                "updated_time varchar(255)," +
//                "alert_status varchar(255)," +
//                "primary key(id) not ENFORCED \n" +
//                ") with ( \n" +
//                "'connector' = 'mysql-cdc', \n" +
//                "'hostname' = '10.68.20.12', \n" +
//                "'port' = '3306', \n" +
//                "'username' = 'root', \n" +
//                "'password' = 'tcl@bigdata', \n" +
//                "'database-name' = 'db_alerts', \n" +
//                "'table-name' = 'alerts' \n" +
//                ")";
        tableEnv.executeSql(testCDCSource);
        Table table = tableEnv.sqlQuery("select * from alerts");
        DataStream<Row> rowDataStream = tableEnv.toChangelogStream(table);
        rowDataStream.print();
        SingleOutputStreamOperator<AlertsEntity> alertsEntityMap = null;
        try {
            alertsEntityMap = rowDataStream.map(x -> {
                String s1 = x.getField(0) != null ? x.getField(0).toString() : "";
                String s2 = x.getField(1) != null ? x.getField(1).toString() : "";
                String s3 = x.getField(2) != null ? x.getField(2).toString() : "";
                String s4 = x.getField(3) != null ? x.getField(3).toString() : "";
                String s5 = x.getField(4) != null ? x.getField(4).toString() : "";
                String s6 = x.getField(5) != null ? x.getField(5).toString() : "";
                String s7 = x.getField(6) != null ? x.getField(6).toString() : "";
                String s8 = x.getField(7) != null ? x.getField(7).toString() : "";
                return new AlertsEntity(s1, s2, s3, s4, s5, s6, s7, s8);
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("-----》数据解析失败");
        }
        alertsEntityMap.print();
//        rowDataStream.print();

        //推送
        alertsEntityMap.addSink(new SinkFunction<AlertsEntity>() {
            @Override
            public void invoke(AlertsEntity value) throws Exception {
                String alertInfo = "【盘古云平台告警】"
                        + "\n[告警名称]: " + value.getName()
                        + "\n[告警等级]: " + value.getLevel()
                        + "\n[告警描述]: " + value.getDescription()
                        + "\n[告警条件]: " + value.getTrigger_condition()
                        + "\n[告警时间]: " + value.getCreated_time()
                        + "\n[恢复时间]: " + value.getUpdated_time();

                System.out.println(alertInfo);

                try {
//                    MyRobotHttpUtil.sendMsg("ai-90110", "Vew2Hcce", "955da14303d8f0b275993ff8586815b0",
//                            "ai-g@10000737847", alertInfo, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("-----》发送告警失败");
                }
            }
        });

        env.execute();

    }

}
