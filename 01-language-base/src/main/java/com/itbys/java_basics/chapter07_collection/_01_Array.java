package com.itbys.java_basics.chapter07_collection;

import java.io.FileNotFoundException;

/**
 * Author Ari
 * Date 2023/3/14
 * Desc
 */
public class _01_Array {

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
