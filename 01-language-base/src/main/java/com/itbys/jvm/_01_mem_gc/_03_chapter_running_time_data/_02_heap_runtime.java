package com.itbys.jvm._01_mem_gc._03_chapter_running_time_data;

/**
 * Author xx
 * Date 2023/3/14
 * Desc
 */
public class _02_heap_runtime {

    public static void main(String[] args) throws InterruptedException {

        //默认byte
        //虚拟机堆内存总量
        long l = Runtime.getRuntime().totalMemory() / 1024 / 1024;
        System.out.println(l);

        //虚拟机堆内存最大量
        long l1 = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        System.out.println(l1);

        Thread.sleep(1000000);

    }

}


