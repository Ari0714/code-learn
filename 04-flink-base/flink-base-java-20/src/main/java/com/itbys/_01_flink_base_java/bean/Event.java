package com.itbys._01_flink_base_java.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author Ari
 * Date 2023/2/19
 * Desc
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    String name;
    String url;
    Long timestamp;

}
