package com.itbys.java_basics.chapter02_oop;

/**
 * Author xx
 * Date 2023/3/14
 * Desc
 */
public class Oop {

    public static void main(String[] args) {

        Oop oop = new Oop();
//        System.out.println(oop instanceof Oop);

        Order order = new Order("aa", 12, '男');
        Order order1 = new Order("aa", 13, '男');
        System.out.println(order.equals(order1));

//        int a = 10;
//        int b = 20;
//        oop.method(a,b);
//        System.out.println(a+","+b);

//        System.out.println(oop.rsum(100));

    }


    //方法一：直接退出，不执行main方法后面的
    public void method(int a, int b) {
        a = 100;
        b = 200;
        System.out.println(a + "," + b);
        System.exit(0);
    }

    //递归：求1-100
    public int rsum(int a) {
        if (a == 1) {
            return 1;
        } else {
            return a + rsum(a - 1);
        }
    }
}
