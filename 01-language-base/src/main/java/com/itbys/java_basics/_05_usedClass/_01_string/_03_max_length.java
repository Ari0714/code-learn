package com.itbys.java_basics._05_usedClass._01_string;

import org.junit.Test;

import java.util.Arrays;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _03_max_length {

    public static void main(String[] args) {

        String str01 = "avdyjukmhellosfsfiliky";
        String str02 = "ryhelloqnot";

        char[] chars = str02.toCharArray();

        String str03 = "";

        for (char c : chars) {
            if (str01.indexOf(c) != -1)
                str03 += c;
        }


    }



    @Test
    public void test01() {

        String a1 = "avdyjukmhellosfsfiliky";

        char[] chars = a1.toCharArray();

        Arrays.sort(chars);

        System.out.println(String.valueOf(chars));

    }


    @Test
    public void test02() {

        String a1 = "       avdyjukmhell osfsfiliky      ";

        char[] chars = a1.toCharArray();

        int min = 99;
        int max = 0;

        for (int i=0;i<chars.length;i++){
            if (chars[i] != ' '){
                if (max < i)
                    max = i;
                if (min > i)
                    min = i;
            }
        }

        System.out.println(a1.substring(min,max));
    }


    @Test
    public void test03() {

        String a1 = "avdyjukmhellosfsfiliky";

        // 8 - 14
        String substring = a1.substring(8, 13);

        char[] chars = substring.toCharArray();

        String a2 = "";

        for (int i=0;i<chars.length;i++){
            a2 += chars[chars.length - i -1];
        }

        System.out.println(a1.replace(substring, a2));
    }


    @Test
    public void test04() {

        String a1 = "abvdyjukmhellosfabsfilikaby";
        System.out.println(a1.split("ab").length - 1);

    }



}
