package com.itbys.java_basics._03_exception;

/**
 * Author xx
 * Date 2023/3/14
 * Desc
 */
public class _01_exception_classes {

    public static void main(String[] args) {

        //nullpoint
        String str = null;
        char a = str.charAt(0);

        //outofindex
        str = "";
        char a1 = str.charAt(0);

        //arithmetic
        int a2 = 0;
        int a3 = 1;
        int i = a3 / a2;

        //numberformat
        int aa = Integer.parseInt("aa");

    }

}
