package com.itbys.java_basics._02_oop;

/**
 * Author xx
 * Date 2023/3/14
 * Desc
 */
public class AbstractTest {

    public static void main(String[] args) {
        Temp absTemp = new AbsTemp();
        absTemp.abs_time();
    }
}

abstract class Temp {

    String aa = "coco";

    public void abs_time() {
        long start_time = System.currentTimeMillis();
        code();
        long end_time = System.currentTimeMillis();
        System.out.println("run time: " + (end_time - start_time));
    }

    public abstract void code();
}


class AbsTemp extends Temp {

    @Override
    public void code() {
        int a = 0;
        for (int i = 0; i <= 1000; i++)
            a += i;
        System.out.println(a);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}