package com.itbys._03_test.aop.anno;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Author xx
 * Date 2023/8/10
 * Desc
 */
public class AspectTest {

    @Test
    public void test01(){

        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("aspect/bean10.xml");
        Person person = classPathXmlApplicationContext.getBean("person", Person.class);
        person.add();

    }

}
