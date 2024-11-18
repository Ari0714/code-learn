package com.itbys.java_basics.chapter02_oop;

import org.junit.Test;

/**
 * Author xx
 * Date 2023/3/14
 * Desc
 */
public class StaticTest {

    @Test
    public void test01() {

//        Chinese.country = "Japan";
//
//        Chinese chinese = new Chinese();
//        chinese.country = "Chinese";
//        chinese.name = "chenj";
//        System.out.println(chinese.country);
//
//        Chinese chinese02 = new Chinese();
//        chinese02.country = "American";
//        chinese02.name = "chenj";
//        System.out.println(chinese02.country);

        Circle circle01 = new Circle();
        Circle circle02 = new Circle();

        System.out.println(circle01.getId());
        System.out.println(circle02.getId());

        System.out.println(Circle.getCnt());

        Circle circle03 = new Circle(66.0);
        System.out.println(circle03.getId());
        System.out.println(Circle.getCnt());


    }
}

class Circle {

    //对象属性私有 ， 类属性公共
    private double radius;
    private int id;

    public static int cnt = 0;
    public static int init = 1001;

    public Circle() {
        id = init++;
        cnt++;
    }

    public Circle(Double radius) {
        this();
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int getCnt() {
        return cnt;
    }

    public static void setCnt(int cnt) {
        Circle.cnt = cnt;
    }

    public static int getInit() {
        return init;
    }

    public static void setInit(int init) {
        Circle.init = init;
    }
}

class Chinese {
    String name;
    static String country;
}

