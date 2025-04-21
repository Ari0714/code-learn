package com.itbys.java_basics.chapter05_usedClass._01_string;


/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _02_used_method {

    public static void main(String[] args) {

        String s = " 123 ";
        System.out.println(s);

        //
        System.out.println(s.length());
        System.out.println(s.isEmpty());

        //
        String str = s.trim();
        System.out.println(str);

        String str01 = str.concat("abc");
        System.out.println(str01);

        String replace = str01.replace('b', 'b');

        String substring = str01.substring(0, 2);

        String s1 = str01.toLowerCase();

        //
        char c = str01.charAt(1);
        int a = str01.indexOf('a');

        String s2 = "a_b_c_d";
        String[] s3 = s2.split("_");
        String s4 = String.valueOf(s3);

        //
        boolean ad = str01.equals("ad");
        String s5 = str01.concat("s");
        boolean a1 = str01.startsWith("a");
        boolean va = str.endsWith("va");


        //capepare
        String c1 = "abc";
        String c2 = "aga";
        System.out.println(c1.compareTo(c2));

        System.out.println(c2.lastIndexOf('a'));

        String s6 = new String(new byte[]{123});

        byte[] bytes = s6.getBytes();

        System.out.println(c1.indexOf('r'));
    }

}
