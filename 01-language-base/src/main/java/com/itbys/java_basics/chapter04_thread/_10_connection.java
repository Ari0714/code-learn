package com.itbys.java_basics.chapter04_thread;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _10_connection {

    public static void main(String[] args) {

        Conn conn = new Conn();

        Thread thread01 = new Thread(conn);
        Thread thread02 = new Thread(conn);
//        Thread thread03 = new Thread(conn);

        thread01.start();
        thread02.start();
//        thread03.start();

    }

}


class Conn implements Runnable {

    private static int num = 100;

    @Override
    public void run() {

        while (true) {

            synchronized (this) {

                notifyAll();

                if (num > 0) {
                    System.out.println(Thread.currentThread().getName() + ": " + num);
                    num--;
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else
                    break;
            }


        }

    }
}