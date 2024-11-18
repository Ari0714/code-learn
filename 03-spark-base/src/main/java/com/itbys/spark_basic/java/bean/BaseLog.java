package com.itbys.spark_basic.java.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Author xx
 * Date 2022/4/11
 * Desc
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseLog implements Serializable {

    String line;
}
