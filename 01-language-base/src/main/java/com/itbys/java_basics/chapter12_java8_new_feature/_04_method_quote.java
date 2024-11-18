package com.itbys.java_basics.chapter12_java8_new_feature;

import org.junit.Test;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _04_method_quote {

    @Test
    // void accept（T t）
    // PrintStream (T t)
    public void test01(){

        Consumer<String> consumer = System.out::println;
        consumer.accept("test01");

    }

    @Test
    public void test02(){

        MQ tom = new MQ("tom");
        Supplier<String> supplier = tom::getName;
        System.out.println(supplier.get());

    }

    //类：：静态方法 && 类：：方法
    @Test
    public void test03(){

        Comparator<Integer> comparator = Integer::compare;
        System.out.println(comparator.compare(11, 22));

        System.out.println("*********************");

        //类：：方法
        Comparator<Integer> comparator02 = Integer::compareTo;
        System.out.println(comparator02.compare(11, 22));

        System.out.println("*********************");

        Function<Double,Long> function = Math::round;
        System.out.println(function.apply(11.3));

    }
}


class MQ{

    String name;

    public MQ(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
