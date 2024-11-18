package com.itbys.java_basics.chapter03_exception;

/**
 * Author xx
 * Date 2023/3/14
 * Desc
 */
public class _04_exception_my {

    public static void main(String[] args) {

        int a = -11;
        if (a > 0)
            System.out.println("正数");
        else
            throw new MyException("MyException");

    }

}


class MyException extends RuntimeException {

    public static final long serialVersionUID = -7034897075645766939L;

    public MyException() {

    }

    public MyException(String message) {
        super(message);
    }
}