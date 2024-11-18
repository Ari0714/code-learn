package com.itbys.java_basics.chapter05_used_classes._01_string;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _01_immutable {

    public static void main(String[] args) {

        //
        String a = "aa";
        String b = "aa";
        System.out.println(a == b);

        //堆地址 ！= 方法区地址
        String aa = new String("aa");
        System.out.println(aa == a);

        //aa字面量 指向一个方法区
        Person aa1 = new Person("aa", 12);
        Person aa2 = new Person("aa", 12);
        System.out.println(aa1.name == aa2.name);

        //
        String b1 = "a";
        String b2 = b1 + "a";
        System.out.println(b2 == a);


    }

    static class Person{

        String name;
        int age;

        public Person() {
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

}



