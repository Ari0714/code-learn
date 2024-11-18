package com.itbys.java_basics.chapter01;

import java.io.FileNotFoundException;

/**
 * Author xx
 * Date 2023/3/14
 * Desc
 */
public class Array {

    public static void main(String[] args) throws FileNotFoundException {

        int[] a = new int[]{1, 3, 4, 5};
        String[] b = new String[]{"aaa", "bbb"};
        String[] c = new String[5];

        for (int i : a) {
            System.out.println(i);
        }
        System.out.println(b[0]);


        String[][] d = new String[][]{{"aa", "bb"}, {"cc", "dd"}};
        for (String[] strings : d) {
            for (String string : strings) {
                System.out.println(string);
            }
        }

        //冒泡。快排


    }

}
