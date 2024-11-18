package com.itbys.java_basics.chapter07_collection;

import java.util.*;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc collection
 */
public class _01_collection {

    public static void main(String[] args) {

        Collection coll = new ArrayList();

        coll.add("aa");
        coll.add(4453L);
        coll.add(new Object());

        int size = coll.size();

        boolean empty = coll.isEmpty();

        Collection coll02 = new ArrayList();

        coll.add("ee");
        coll.add(31);

        coll.addAll(coll02);

        System.out.println(coll);

        boolean aa = coll.contains("aa");
        boolean b = coll.containsAll(coll02);

        boolean aa1 = coll.remove("aa");
        boolean b1 = coll.removeAll(coll02);

        boolean b2 = coll.retainAll(coll02);

        Object[] objects = coll.toArray();

        List<String> strings = Arrays.asList(new String[]{"da", "gg"});


        //
        Collection coll03 = new ArrayList();

        coll03.add("aa");
        coll03.add(4453L);
        coll03.add(new Object());

        Iterator iterator = coll03.iterator();

        while (iterator.hasNext())
            System.out.println(iterator.next());

        for (Object obj : coll03)
            System.out.println(obj);

//        coll.clear();

    }


}
