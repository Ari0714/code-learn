package com.itbys.java_basics.chapter04_thread;

/**
 * Author xx
 * Date 2023/3/14
 * Desc
 */
public class _03_runnable {

    public static void main(String[] args) {

        B b = new B();
        Thread thread = new Thread(b);
        thread.start();

        //多
        Thread thread1 = new Thread(b);
        thread1.start();

    }

}


class B implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i <= 100; i++)
            if (i % 3 == 0)
                System.out.println(Thread.currentThread().getName() + ": " + i);
    }
}