package com.itbys.java_basics.chapter02_oop._02_interface;

/**
 * Author xx
 * Date 2023/3/14
 * Desc
 */
public class _03_inter_class {

    public static void main(String[] args) {

        //局部内部类
        class Animal{

        }

    }

    //成员内部类
    final class Dog{
        String name;
        void bark(){}
    }


    abstract static class Cat{
        String name;
        void miaow(){}
    }


}


