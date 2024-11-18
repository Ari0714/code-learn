package com.itbys.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Author chenjie
 * Date 2024/7/17
 * Desc
 */
public class MyPropertiesUtilJ {

    public static void main(String[] args) {
        Properties load = load("log4j.properties");
        System.out.println(load.getProperty("log4j.rootLogger"));
    }


    /**
     * @desc 读取配置文件
     * @param: fileName
     * @return: java.com.itbys.util.Properties
     */
    public static Properties load(String fileName) {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }


}
