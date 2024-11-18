package com.itbys.java_basics.chapter04_thread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _08_lock {

    public static void main(String[] args) {

        Tickets04 tickets04 = new Tickets04();

        Thread thread01 = new Thread(tickets04);
        Thread thread02 = new Thread(tickets04);
        Thread thread03 = new Thread(tickets04);

        thread01.start();
        thread02.start();
        thread03.start();

    }

}


class Tickets04 implements Runnable {

    private static int TicketNum = 100;

    private ReentrantLock lock = new ReentrantLock();

    @Override
    public void run() {
        while (true) {
            try {
                lock.lock();
                if (TicketNum > 0) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("买出一张票，还剩: " + --TicketNum);
                } else
                    break;
            } finally {
                lock.unlock();
            }

        }
    }
}