package com.itbys._03_test.aop.anno;

import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Author xx
 * Date 2022/7/31
 * Desc
 */
@Component
@Aspect  //开启代理对象
@Order(1) //多个增强类对同一个方法增强，设置优先级
public class PersonProxy2 {

    @Before(value = "execution(* com.itbys._03_test.aop.anno.Person.add(..))")
    public void before(){
        System.out.println("Before PersonProxy2...");
    }


}
