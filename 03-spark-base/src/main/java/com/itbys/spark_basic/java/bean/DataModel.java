package com.itbys.spark_basic.java.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Author chenj
 * Date 2021/8/2
 * Desc
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataModel implements Serializable {

    private String id;
    private String item;
    private String category;
    private String behavior;
    private String dt;
    private Integer hourr;


}
