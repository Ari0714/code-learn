package com.itbys.java_basics._12_java8_new_feature;

import org.junit.Test;

import java.util.Comparator;
import java.util.function.Consumer;


/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
// 左边参数列表  ->  方法体
// 参数  返回值
// lamdba本质: 接口的实例
public class _02_lamdba_6classes {

    //无参 无返回值
    @Test
    public void test01() {

        Runnable runnable = () -> System.out.println("test01");
        runnable.run();
    }

    //一参 无返回值
    @Test
    public void test02() {

        Consumer<String> stringConsumer = (String str) -> System.out.println(str);
        stringConsumer.accept("test02");
    }

    //一参 无返回值
    // 数据类型省 推断
    @Test
    public void test03() {

        Consumer<String> stringConsumer = (str) -> System.out.println(str);
        stringConsumer.accept("test03");
    }

    //一参 无返回值
    // 数据类型省 推断
    // 一参 （） 省
    @Test
    public void test04() {

        Consumer<String> stringConsumer = str -> System.out.println(str);
        stringConsumer.accept("test04");

    }


    //一参 无返回值
    // 数据类型省 推断
    // 一参 （） 省
    // 一条语句 {} 省
    @Test
    public void test05() {

        Comparator<Integer> comparator = (Integer o1, Integer o2) -> o1.compareTo(o2);
        System.out.println(comparator.compare(11, 22));

    }





}


