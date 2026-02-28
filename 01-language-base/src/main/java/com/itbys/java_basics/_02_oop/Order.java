package com.itbys.java_basics._02_oop;

import java.util.Objects;

/**
 * Author Ari
 * Date 2023/3/14
 * Desc
 */
public class Order {

    String name;
    int age;
    char sex;
    boolean aa;
    Boolean bb;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return getAge() == order.getAge() &&
                getSex() == order.getSex() &&
                Objects.equals(getName(), order.getName());
    }


    //    @Override
//    public boolean equals(Object obj) {
//        if (this == obj)
//            return true;
//        if (obj instanceof Order){
//            Order order = (Order) obj;
//            if (this.getName().equals(order.getName()) && this.getAge() == order.getAge() && this.getSex() == order.getSex())
//                return true;
//            return false;
//        }
//        return false;
//    }


    public Order() {
    }

    public Order(String name, int age, char sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }
}
