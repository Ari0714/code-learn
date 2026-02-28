package com.itbys.java_basics._07_collection;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc map
 */
public class _04_map {

    public static void main(String[] args) {

        HashMap hashMap = new HashMap();
        Hashtable hashtable = new Hashtable();

        hashMap.put("aa", 12);
        hashMap.put("bb", 67);

        hashMap.put(null, null);
//        hashtable.put(null,null);

        hashMap.equals(hashMap);

        Set set = hashMap.keySet();

        Collection values = hashMap.values();

        Set set1 = hashMap.entrySet();

        Iterator iterator = set1.iterator();

//        while (iterator.hasNext())
//            System.out.println(iterator.next());

        for (Object o : set) {
            System.out.println(o + "-->" + hashMap.get(0));
        }

    }


    @Test
    public void test01() throws IOException {

        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream("input.txt/pro.properties");
        properties.load(fileInputStream);

        String name = properties.getProperty("name");
        String cname = properties.getProperty("cname");
        String age = properties.getProperty("age");

        System.out.println(name + "_" + cname + "_" + age);

    }


}
