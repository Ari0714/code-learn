package com.itbys.spark_basic.java.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import scala.Serializable;

/**
 * Author xx
 * Date 2022/4/11
 * Desc
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopicEventLog implements Serializable {

    String what_action;
    String what_content;
    String what_description;
    String what_expand1;
    String what_expand2;
    String what_expand3;
    String when_start;
    String when_end;
    String when_expand1;
    String when_expand2;
    String when_expand3;
    String where_corpid;
    String where_clientinfo;
    String where_clienttype;
    String where_entrance;
    String where_module;
    String where_expand1;
    String where_expand2;
    String where_expand3;
    String who_id;
    String who_externaluserid;
    String who_role;
    String who_selfexternaluserid;
    String who_selfunionid;
    String who_source;
    String who_unionid;
    String who_expand1;
    String who_expand2;
    String who_expand3;
    String whom_id;
    String whom_externaluserid;
    String whom_role;
    String whom_selfexternaluserid;
    String whom_selfunionid;
    String whom_source;
    String whom_unionid;
    String whom_expand1;
    String whom_expand2;
    String whom_expand3;
    String ds;

}
