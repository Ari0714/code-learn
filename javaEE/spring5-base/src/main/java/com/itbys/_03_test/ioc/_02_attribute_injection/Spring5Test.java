package com.itbys._03_test.ioc._02_attribute_injection;


import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Author xx
 * Date 2022/7/31
 * Desc
 */
public class Spring5Test {

    // 属性注入
    @Test
    public void test11() {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig2.class);
        UserService2 userService = context.getBean("userService2", UserService2.class);
        userService.add();
    }



}
