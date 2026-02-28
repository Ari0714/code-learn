package com.itbys.java_basics._03_exception;

/**
 * Author xx
 * Date 2023/3/14
 * Desc
 */
public class _02_exception_handle {

    public static void main(String[] args) {

        try {
            int aa = Integer.parseInt("11");

            //前面异常程序终止，后面不运行
            int b1 = 1;
            int b2 = 0;
            int b3 = 2;
            int i = b1 / b3;
        } catch (NumberFormatException e) {
//            e.printStackTrace();
            System.out.println("NumberFormatException异常");
        } catch (ArithmeticException e) {
            System.out.println("ArithmeticException异常");
        } finally {
            System.out.println("代码包含异常处理");
        }

    }
}
