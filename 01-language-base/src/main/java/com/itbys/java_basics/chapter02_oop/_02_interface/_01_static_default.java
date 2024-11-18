package com.itbys.java_basics.chapter02_oop._02_interface;

/**
 * Author xx
 * Date 2023/3/14
 * Desc
 */
public class _01_static_default {

    public static void main(String[] args) {

        Say.pt();
        new Dog().df();

    }
}


class Animal {

}


class Dog extends Animal implements DogBark, Say {

    @Override
    public void bark() {
        System.out.println("barkkkkkkkk!!!");
    }

    @Override
    public void df() {
        System.out.println("default method22222");
    }
}


interface DogBark {
    void bark();
}


interface Say {

    static void pt() {
        System.out.println("static method");
    }

    default void df() {
        System.out.println("default method");
    }
}