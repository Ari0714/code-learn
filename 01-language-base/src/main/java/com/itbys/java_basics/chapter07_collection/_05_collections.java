package com.itbys.java_basics.chapter07_collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc collections utils
 */
public class _05_collections {

    public static void main(String[] args) {

        ArrayList<String> strings = new ArrayList<>();
        strings.add("aa");
        strings.add("gh");
        strings.add("cc");
        strings.add("ad");
        strings.add("bb");

//        strings.sort(new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                return o2.compareTo(o1);
//            }
//        });

        Collections.sort(strings, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof String && o2 instanceof String) {
                    String s1 = (String) o1;
                    String s2 = (String) o2;
                    return -s1.compareTo(s2);
                }
                throw new RuntimeException("输入类型有误");
            }
        });

        for (String string : strings) {
            System.out.println(string);
        }

    }


}
