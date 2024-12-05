package com.itbys._01_flink_base_java.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.FunctionHint;
import org.apache.flink.table.functions.TableFunction;
import org.apache.flink.types.Row;

/**
 * Author jie173.chen
 * Date 2024/11/26
 * Desc
 */
@FunctionHint(output = @DataTypeHint("ROW<mapp STRING>"))
public class UnnestStringArrayFunction extends TableFunction<Row> {
    public void eval(String input) {
        if (input != null) {
            JSONArray objects = JSON.parseArray(input);
            for (Object object : objects) {
                collect(Row.of(object.toString()));
            }
        }
    }


    /**
     * create temporary function unnestStringArray as 'com.itbys._01_flink_base_java.self_udf.UnnestStringArrayFunction';
     * 1、input type align with real data type
     *
     */

}