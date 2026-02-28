package com.itbys.java_basics._02_oop;

/**
 * Author xx
 * Date 2023/3/14
 * Desc
 */
public class SingletonTest {

    public static void main(String[] args) {
        SingleClass instance01 = SingleClass.getInstance();
        SingleClass instance02 = SingleClass.getInstance();
        System.out.println(instance01 == instance02);
    }

}


class SingleClass {

    private SingleClass() {

    }

    //饿汉式
//    private static SingleClass singleClass = new SingleClass();
//
//    public static SingleClass getInstance(){
//        return singleClass;
//    }

    //懒汉式
    private static SingleClass singleClass = null;

    public static SingleClass getInstance() {
        if (singleClass == null)
            singleClass = new SingleClass();
        return singleClass;
    }


}