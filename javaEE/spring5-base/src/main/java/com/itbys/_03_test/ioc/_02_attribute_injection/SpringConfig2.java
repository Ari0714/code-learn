package com.itbys._03_test.ioc._02_attribute_injection;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Author xx
 * Date 2022/7/31
 * Desc
 */
@Configuration  //作为配置类，替换xml配置文件
@ComponentScan(basePackages = {"com.itbys._03_test.ioc._02_attribute_injection"})
public class SpringConfig2 {
}
