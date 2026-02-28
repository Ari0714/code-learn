package com.itbys.java_basics._02_oop;

/**
 * Author xx
 * Date 2023/3/14
 * Desc
 */
public class Block {

    public static void main(String[] args) {
        String desc = Person.desc;
        System.out.println(desc);
    }

}


class Person {

    String name;
    int age;
    static String desc = "abcd";

    public Person() {
    }

    static {
        System.out.println("this is static block01!!!");
    }

    static {
        System.out.println("this is static block02!!!");
    }

}