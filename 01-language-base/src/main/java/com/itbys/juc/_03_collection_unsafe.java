package com.itbys.juc;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Author Ari
 * Date 2024/7/15
 * Desc
 */
public class _03_collection_unsafe {

    public static void main(String[] args) {

        //java.com.tcl.com.itbys.util.ConcurrentModificationException
        List<String> arrayList99 = new ArrayList<>();

        //解决1
        List<String> arrayList01 = new Vector<>();
        //解决2
        List<String> arrayList02 = Collections.synchronizedList(new ArrayList<>());
        //解决3
        List<String> arrayList = new CopyOnWriteArrayList<>();


        for (int i = 0; i < 30; i++) {
            new Thread(
                    () -> {
                        arrayList.add(UUID.randomUUID().toString().substring(0, 8));
                        System.out.println(arrayList);
                    }, String.valueOf(i)).start();
        }


    }

}
