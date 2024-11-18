package com.itbys._03_test.aop.calculator.impl;

import com.itbys._03_test.aop.calculator.Calculator;

/**
 * Author xx
 * Date 2023/2/18
 * Desc
 */
public class CalculatorImpl implements Calculator {

    @Override
    public void add(int a, int b) {
        int res = a + b;
        System.out.println(res);
    }

    @Override
    public void sub(int a, int b) {
        int res = a - b;
        System.out.println(res);
    }

    @Override
    public void mul(int a, int b) {
        int res = a * b;
        System.out.println(res);
    }

    @Override
    public void div(int a, int b) {
        int res = a / b;
        System.out.println(res);
    }
}
