package com.itbys.java_basics._02_oop._02_interface;

/**
 * Author xx
 * Date 2023/3/14
 * Desc
 */
public class _02_default_conflict {

    public static void main(String[] args) {
        new Man().help();
    }

}


interface Filial {
    default void help() {
        System.out.println("救妈妈！！！");
    }
}

interface Spoony {
    default void help() {
        System.out.println("救妈妈！！！");
    }
}


//有extends 使用extends中 ， 无重写 或 接口名.super.method名区分调用
class Man implements Filial, Spoony {

    @Override
    public void help() {
        Filial.super.help();
    }
}