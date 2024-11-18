package com.itbys.java_basics.chapter04_thread;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _01_thread {

    public static void main(String[] args) {

        A a = new A();
        //start 启动当前线程，调用run方法
//        a.run();
        a.start();

        A a1 = new A();
        a1.start();

        a1.getPriority();

//        System.out.println(Thread.currentThread().getName()+": " + "mainnnnn");

        for (int i = 0; i <= 100; i++) {
            if (i % 10 == 0) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
                try {
                    a.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}


class A extends Thread {
    @Override
    public void run() {
        for (int i = 0; i <= 100; i++)
            if (i % 3 == 0)
                System.out.println(Thread.currentThread().getName() + ": " + i);
    }
}