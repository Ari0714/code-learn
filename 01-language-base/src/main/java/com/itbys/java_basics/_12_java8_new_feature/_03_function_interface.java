package com.itbys.java_basics._12_java8_new_feature;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _03_function_interface {

    public void apply(Double money,Consumer<Double> consumer){

        consumer.accept(money);

    }

    @Test
    public void test01(){

        apply(200.0,money -> System.out.println("水费："+money));

    }


    public List<String> check (List<String> list, Predicate<String> predicate){

        ArrayList<String> arrayList = new ArrayList<>();

        for (String s : list) {
            if (predicate.test(s))
                arrayList.add(s);
        }

        return arrayList;
    }

    @Test
    public void test02(){

        List<String> strings = Arrays.asList("aa", "bb", "cc", "dd");

        List<String> check = check(strings, str -> str.contains("cc"));

        for (String s : check) {
            System.out.println(s);
        }
    }


}
