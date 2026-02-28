package com.itbys.java_basics._12_java8_new_feature;

import org.junit.Test;

import java.util.Comparator;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _01_lamdba {

    public static void main(String[] args) {

    }

    @Test
    public void test01(){

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("test01");
            }
        };

        runnable.run();

        System.out.println("*********************");

        Runnable runnable1 = () -> System.out.println("test01_lamdba");
        runnable1.run();

    }

    @Test
    public void test02(){

        Comparator<Integer> comparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        };

        System.out.println(comparator.compare(11, 22));

        System.out.println("*********************");

        // -> lamdba表达式
        Comparator<Integer> comparator1 = (o1,o2) -> Integer.compare(o1,o2);
        System.out.println(comparator1.compare(11, 22));

        System.out.println("*********************");

        // :: 方法引用
        Comparator<Integer> comparator2 = Integer::compareTo;
        System.out.println(comparator2.compare(11, 22));

    }



}
