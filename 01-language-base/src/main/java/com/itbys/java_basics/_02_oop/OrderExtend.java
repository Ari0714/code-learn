package com.itbys.java_basics._02_oop;

/**
 * Author Ari
 * Date 2024/2/26
 * Desc
 */
public class OrderExtend extends Order {

    String info;

    public OrderExtend(String name, int age, char sex, String info) {
        super(name, age, sex);
        this.info = info;
    }

    @Override
    public String getName() {
        return super.getName();
    }
}
