package com.itbys.java_basics._02_oop;

import org.junit.Test;

/**
 * Author xx
 * Date 2023/3/14
 * Desc
 */
public class WrapperTest {

    @Test
    public void test01() {

        //wrapper =》 string    基本 =》 wrapper
        int a = 1;
        Integer integer = new Integer(a);
        String s = integer.toString();

        Integer integer02 = new Integer("1");

        Order order = new Order();
        boolean aa = order.aa;
        Boolean bb = order.bb;
        System.out.println(bb);

    }

    @Test
    public void test02() {
        // xxValue
        Integer integer = new Integer(123);
        int i = integer.intValue();
        Float aFloat = new Float(3.134f);
        float v = aFloat.floatValue();

        //自动装箱 拆箱 jdk 5.0 之后
        int aa = 5;
        Integer bb = aa;

        int cc = bb;

    }

    @Test
    public void test03() {
        int aa = 33;
        String s = String.valueOf(33);

        int i = Integer.parseInt(s);
    }

    @Test
    public void test04() {
        //三元运算类型一致
        Object obj = true ? new Integer(1) : new Double(2.0);
        System.out.println(obj);

    }

}

