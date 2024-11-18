package com.itbys.java_basics.chapter05_used_classes._02_date;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _02_date {

    public static void main(String[] args) {

        // 1870 -1 -1
        long l = System.currentTimeMillis();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long l02 = System.currentTimeMillis();
        System.out.println(l02 - l);


        //Date  Sat Feb 20 14:36:05 CST 2021
        Date date = new Date(l);
        long time = date.getTime();

        //simpleDateFormat
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat.format(date));


//        new Calendar();

        //
        System.out.println(LocalDateTime.now().getMonth());


    }

}

