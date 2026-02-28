package com.itbys.java_basics._07_collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc list
 */
public class _02_list {

    public static void main(String[] args) {

        ArrayList<String> strings = new ArrayList<>();

        strings.add("aa");
        strings.add(1,"bb");
        strings.set(2,"cc");
        String s = strings.get(2);
        int cc = strings.indexOf("cc");
        List<String> strings1 = strings.subList(1, 2);
        strings.remove(1);

        //
        for (String string : strings) {
            System.out.println(string);
        }

        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext())
            System.out.println(iterator.next());

    }

}
