package com.itbys.java_basics._03_exception;

public class _05_exception_throw {

    public static void main(String[] args) {

        A a = new A(-12);

    }
}


class A {

    int a;

    public A(int a) {
        if (a > 0)
            this.a = a;
        else
            throw new RuntimeException("输入数据不合法！！！");
    }
}