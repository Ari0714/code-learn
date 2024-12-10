package com.itbys._03_test.sql_test;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * Author chenjie
 * Date 2023/11/28
 * Desc 测试flink sql写入hana
 */
public class HanaTest {

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


        tableEnv.executeSql("CREATE TABLE mysql_source_table (\n" +
                "  `order_finish_id` string ,\n" +
                "  `order_no` string ,\n" +
                "  `finish_way_code` string ,\n" +
                "  `finish_way_name` string ,\n" +
                "  `service_method_code` string ,\n" +
                "  `service_method_name` string ,\n" +
                "  `service_type_code` string ,\n" +
                "  `service_type_name` string ,\n" +
                "  `repair_method_type_code` string ,\n" +
                "  `repair_method_type_name` string ,\n" +
                "  `settle_type_code` string ,\n" +
                "  `settle_type_name` string ,\n" +
                "  `product_model_type_code` string ,\n" +
                "  `product_model_type_name` string ,\n" +
                "  `test_fault_code` string ,\n" +
                "  `test_fault_name` string ,\n" +
                "  `warranty_type_code` string ,\n" +
                "  `warranty_type_name` string ,\n" +
                "  `batch_no` string ,\n" +
                "  `product_sku_code` string ,\n" +
                "  `product_sku_name` string ,\n" +
                "  `microchip_name` string ,\n" +
                "  `barcode` string ,\n" +
                "  `product_barcode` string ,\n" +
                "  `product_barcode2` string ,\n" +
                "  `new_product_sku_code` string ,\n" +
                "  `new_product_sku_name` string ,\n" +
                "  `product_attr` string ,\n" +
                "  `display_screen_material_code` string ,\n" +
                "  `display_screen_material_name` string ,\n" +
                "  `power_supply_material_code` string ,\n" +
                "  `power_supply_material_name` string ,\n" +
                "  `movement_material_code` string ,\n" +
                "  `movement_material_name` string ,\n" +
                "  `odf` string ,\n" +
                "  `new_microchip_name` string ,\n" +
                "  `new_product_barcode` string ,\n" +
                "  `new_product_barcode2` string ,\n" +
                "  `service_bill_type_code` string ,\n" +
                "  `service_bill_type_name` string ,\n" +
                "  `is_finish_lock` string ,\n" +
                "  `finish_check_code` string ,\n" +
                "  `finish_check_name` string ,\n" +
                "  `finish_check_date` string ,\n" +
                "  `finish_check_reason` string ,\n" +
                "  `mach_review_fail_reason` string ,\n" +
                "  `is_normal_package` string ,\n" +
                "  `is_lack_attach` string ,\n" +
                "  `attach_lack_remark` string ,\n" +
                "  `is_normal_power_test` string ,\n" +
                "  `is_normal_appearance` string ,\n" +
                "  `is_normal_environment` string ,\n" +
                "  `is_change_panel` string ,\n" +
                "  `panel_name` string ,\n" +
                "  `is_change_guideplate` string ,\n" +
                "  `guideplate_name` string ,\n" +
                "  `resp_id` string ,\n" +
                "  `resp_name` string ,\n" +
                "  `reject_reason_id` string ,\n" +
                "  `reject_reason_name` string ,\n" +
                "  `equity_use_record` string ,\n" +
                "  `equity_name` string ,\n" +
                "  `equity_seq_no` string ,\n" +
                "  `equity_invalid_date` string ,\n" +
                "  `equity_usable` string ,\n" +
                "  `is_praise` string ,\n" +
                "  `software_version` string ,\n" +
                "  `main_parts_warranty_date` string ,\n" +
                "  `whole_warranty_date` string ,\n" +
                "  `extended_warranty_code` string ,\n" +
                "  `is_submit` string ,\n" +
                "  `remark` string ,\n" +
                "  `buy_time` string ,\n" +
                "  `first_finish_time` string ,\n" +
                "  `finish_time` string ,\n" +
                "  `sign_parts_time` string ,\n" +
                "  `machine_back_reason_code` string ,\n" +
                "  `machine_back_reason_name` string ,\n" +
                "  `living_floor` string ,\n" +
                "  `floor_height` string ,\n" +
                "  `floor_area` string ,\n" +
                "  `entry_method` string ,\n" +
                "  `survey_results` string ,\n" +
                "  `wall_survey` string ,\n" +
                "  `install_way` string ,\n" +
                "  `is_normal_whole_test` string ,\n" +
                "  `is_special_environment` string ,\n" +
                "  `settle_rule_status_code` string ,\n" +
                "  `settle_rule_status_name` string ,\n" +
                "  `settle_rule_match_msg` string ,\n" +
                "  `barcode1` string ,\n" +
                "  `barcode2` string ,\n" +
                "  `barcode_status_code` string ,\n" +
                "  `barcode_status_name` string ,\n" +
                "  `unable_install_reason_code` string ,\n" +
                "  `unable_install_reason_name` string ,\n" +
                "  `install_env_code` string ,\n" +
                "  `install_env_name` string ,\n" +
                "  `door_material_code` string ,\n" +
                "  `door_material_name` string ,\n" +
                "  `door_body_thickness` string ,\n" +
                "  `door_crevice_width` string ,\n" +
                "  `lock_cylinder_margin` string ,\n" +
                "  `piece_size_length` string ,\n" +
                "  `piece_size_width` string ,\n" +
                "  `equipment_model` string ,\n" +
                "  `active_time` string ,\n" +
                "  `version_no` string ,\n" +
                "  `ent_id` string ,\n" +
                "  `ent_name` string ,\n" +
                "  `created_account` string ,\n" +
                "  `created_name` string ,\n" +
                "  `creation_date` string ,\n" +
                "  `last_updated_account` string ,\n" +
                "  `last_updated_name` string ,\n" +
                "  `last_update_date` string ,\n" +
                "  `au_last_update_time` string \n" +
                ") WITH (\n" +
                "   'connector' = 'jdbc',\n" +
                "   'url' = 'jdbc:mysql://10.126.124.44:4000/data_asset?characterEncoding=utf-8&useSSL=false',\n" +
                "   'driver' = 'com.mysql.jdbc.Driver',  \n" +
                "   'username' = 'dataasset',\n" +
                "   'password' = 'Da@20232',\n" +
                "   'table-name' = 'dwd_cbg_cs_case_detail'\n" +
                ")");


        tableEnv.executeSql("CREATE TABLE pg_sink_table (\n" +
                "  `order_finish_id` string ,\n" +
                "  `order_no` string ,\n" +
                "  `finish_way_code` string ,\n" +
                "  `finish_way_name` string ,\n" +
                "  `service_method_code` string ,\n" +
                "  `service_method_name` string ,\n" +
                "  `service_type_code` string ,\n" +
                "  `service_type_name` string ,\n" +
                "  `repair_method_type_code` string ,\n" +
                "  `repair_method_type_name` string ,\n" +
                "  `settle_type_code` string ,\n" +
                "  `settle_type_name` string ,\n" +
                "  `product_model_type_code` string ,\n" +
                "  `product_model_type_name` string ,\n" +
                "  `test_fault_code` string ,\n" +
                "  `test_fault_name` string ,\n" +
                "  `warranty_type_code` string ,\n" +
                "  `warranty_type_name` string ,\n" +
                "  `batch_no` string ,\n" +
                "  `product_sku_code` string ,\n" +
                "  `product_sku_name` string ,\n" +
                "  `microchip_name` string ,\n" +
                "  `barcode` string ,\n" +
                "  `product_barcode` string ,\n" +
                "  `product_barcode2` string ,\n" +
                "  `new_product_sku_code` string ,\n" +
                "  `new_product_sku_name` string ,\n" +
                "  `product_attr` string ,\n" +
                "  `display_screen_material_code` string ,\n" +
                "  `display_screen_material_name` string ,\n" +
                "  `power_supply_material_code` string ,\n" +
                "  `power_supply_material_name` string ,\n" +
                "  `movement_material_code` string ,\n" +
                "  `movement_material_name` string ,\n" +
                "  `odf` string ,\n" +
                "  `new_microchip_name` string ,\n" +
                "  `new_product_barcode` string ,\n" +
                "  `new_product_barcode2` string ,\n" +
                "  `service_bill_type_code` string ,\n" +
                "  `service_bill_type_name` string ,\n" +
                "  `is_finish_lock` string ,\n" +
                "  `finish_check_code` string ,\n" +
                "  `finish_check_name` string ,\n" +
                "  `finish_check_date` string ,\n" +
                "  `finish_check_reason` string ,\n" +
                "  `mach_review_fail_reason` string ,\n" +
                "  `is_normal_package` string ,\n" +
                "  `is_lack_attach` string ,\n" +
                "  `attach_lack_remark` string ,\n" +
                "  `is_normal_power_test` string ,\n" +
                "  `is_normal_appearance` string ,\n" +
                "  `is_normal_environment` string ,\n" +
                "  `is_change_panel` string ,\n" +
                "  `panel_name` string ,\n" +
                "  `is_change_guideplate` string ,\n" +
                "  `guideplate_name` string ,\n" +
                "  `resp_id` string ,\n" +
                "  `resp_name` string ,\n" +
                "  `reject_reason_id` string ,\n" +
                "  `reject_reason_name` string ,\n" +
                "  `equity_use_record` string ,\n" +
                "  `equity_name` string ,\n" +
                "  `equity_seq_no` string ,\n" +
                "  `equity_invalid_date` string ,\n" +
                "  `equity_usable` string ,\n" +
                "  `is_praise` string ,\n" +
                "  `software_version` string ,\n" +
                "  `main_parts_warranty_date` string ,\n" +
                "  `whole_warranty_date` string ,\n" +
                "  `extended_warranty_code` string ,\n" +
                "  `is_submit` string ,\n" +
                "  `remark` string ,\n" +
                "  `buy_time` string ,\n" +
                "  `first_finish_time` string ,\n" +
                "  `finish_time` string ,\n" +
                "  `sign_parts_time` string ,\n" +
                "  `machine_back_reason_code` string ,\n" +
                "  `machine_back_reason_name` string ,\n" +
                "  `living_floor` string ,\n" +
                "  `floor_height` string ,\n" +
                "  `floor_area` string ,\n" +
                "  `entry_method` string ,\n" +
                "  `survey_results` string ,\n" +
                "  `wall_survey` string ,\n" +
                "  `install_way` string ,\n" +
                "  `is_normal_whole_test` string ,\n" +
                "  `is_special_environment` string ,\n" +
                "  `settle_rule_status_code` string ,\n" +
                "  `settle_rule_status_name` string ,\n" +
                "  `settle_rule_match_msg` string ,\n" +
                "  `barcode1` string ,\n" +
                "  `barcode2` string ,\n" +
                "  `barcode_status_code` string ,\n" +
                "  `barcode_status_name` string ,\n" +
                "  `unable_install_reason_code` string ,\n" +
                "  `unable_install_reason_name` string ,\n" +
                "  `install_env_code` string ,\n" +
                "  `install_env_name` string ,\n" +
                "  `door_material_code` string ,\n" +
                "  `door_material_name` string ,\n" +
                "  `door_body_thickness` string ,\n" +
                "  `door_crevice_width` string ,\n" +
                "  `lock_cylinder_margin` string ,\n" +
                "  `piece_size_length` string ,\n" +
                "  `piece_size_width` string ,\n" +
                "  `equipment_model` string ,\n" +
                "  `active_time` string ,\n" +
                "  `version_no` string ,\n" +
                "  `ent_id` string ,\n" +
                "  `ent_name` string ,\n" +
                "  `created_account` string ,\n" +
                "  `created_name` string ,\n" +
                "  `creation_date` string ,\n" +
                "  `last_updated_account` string ,\n" +
                "  `last_updated_name` string ,\n" +
                "  `last_update_date` string ,\n" +
                "  `au_last_update_time` string\n" +
                ") WITH (\n" +
                "   'connector' = 'jdbc',\n" +
                "   'url' = 'jdbc:postgresql://hgprecn-cn-tl32n6qol005-cn-hangzhou.hologres.aliyuncs.com:80/bigdatadwcn',\n" +
                "   'driver' = 'org.postgresql.Driver',  \n" +
                "   'username' = 'BASIC$tmp_dtc_user01',\n" +
                "   'password' = 'M2yQkh7ccUdzV9Nc',\n" +
                "   'table-name' = 'temp.dwd_cbg_cs_case_detail'\n" +
                ")");


        tableEnv.executeSql("INSERT INTO pg_sink_table\n" +
                "SELECT * from mysql_source_table");



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





//        env.execute();

    }

}
