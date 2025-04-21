package com.itbys._01_flink_base_java.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author Ari
 * Date 2024/6/7
 * Desc
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertsEntity {

    //  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
    //  `name` varchar(255) NOT NULL COMMENT '告警的名称',
    //  `level` varchar(50) NOT NULL COMMENT '告警等级：警告(Warning)，一般(Average)，严重(High)',
    //  `description` text COMMENT '告警的描述',
    //  `trigger_condition` text COMMENT '触发告警的条件',
    //  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '告警时间',
    //  `updated_time` varchar(50) DEFAULT NULL COMMENT '恢复时间',
    //  `alert_status` varchar(50) DEFAULT '0' COMMENT '是否推送告警，0 未推送，1 已推送',
    String id;
    String name;
    String level;
    String description;
    String trigger_condition;
    String created_time;
    String updated_time;
    String alert_status;

}
