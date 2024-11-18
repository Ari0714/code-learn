package com.itbys.jvm._01_mem_gc._03_chapter_running_time_data;

/**
 * Author xx
 * Date 2023/3/14
 * Desc
 */
public class _01_stack_error {

    public static int a = 0;

    //11401
    //-Xss256k  2463
    public static void main(String[] args) {

        System.out.println(a);
        a++;
        main(args);

    }

}
