package com.itbys.java_basics.chapter12_java8_new_feature;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _04_Stream_from {

    @Test
    public void Test01(){

        //collection
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5, 6);
        //顺序流
        Stream<Integer> stream = integers.stream();
        //并行流
        Stream<Integer> parallel = stream.parallel();

        //Array
        int[] ints = {1, 2, 3, 4, 5, 6};
        IntStream stream1 = Arrays.stream(ints);

        //静态方法 .of
        Stream<String> aa = Stream.of("aa", "bb", "cc");

    }


    @Test
    public void test02(){

        Stream<Integer> stream = Arrays.asList(1, 2, 3, 4, 5, 6).stream();

        Stream<Integer> integerStream = stream.filter(x -> x > 2);
//        integerStream.forEach(System.out::println);

        Stream<Integer> integerStream02 = integerStream.limit(3);
//        integerStream02.forEach(System.out::println);

        Stream<Integer> integerStream03 = integerStream02.skip(2);
        integerStream03.forEach(System.out::println);

        Stream<Integer> stream02 = Arrays.asList(1, 2, 3, 3, 5, 1).stream();
        stream02.distinct().forEach(System.out::println);

    }


    @Test
    public void test03(){

        Stream<String> stream = Arrays.asList("aa", "bb", "cc", "dd").stream();
        Stream<String> stringStream = stream.map(s -> s.toUpperCase());
        stringStream.forEach(System.out::println);

        List<String> strings = Arrays.asList("11", "22", "33", "44");
        ArrayList arrayList = new ArrayList();
        arrayList.add(strings);
        arrayList.add("99");

        Stream stream1 = arrayList.stream();
//        stream1.flatMap(x -> x.)

    }


}
