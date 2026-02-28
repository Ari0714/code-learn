package com.itbys.java_basics._04_thread;

/**
 * Author xx
 * Date 2023/3/14
 * Desc
 */
public class _05_sale_tickets_safe {

    public static void main(String[] args) {

        Tickets02 Tickets02 = new Tickets02();
        Thread thread01 = new Thread(Tickets02);
        Thread thread02 = new Thread(Tickets02);
        Thread thread03 = new Thread(Tickets02);

        thread01.start();
        thread02.start();
        thread03.start();
    }

}

class Tickets02 implements Runnable {

    private static int TicketNum = 100;
    Object obj = new Object();

    @Override
    public void run() {

        while (true) {
            synchronized (this) {
                if (TicketNum > 0) {

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println(Thread.currentThread().getName() + "买出一张票，还剩: " + --TicketNum);
                } else
                    break;
            }
        }
    }
}


class Tickets03 implements Runnable {

    private static int TicketNum = 100;
    Object obj = new Object();

    @Override
    public void run() {

        while (true) {
            show();
        }
    }

    public synchronized void show() {
        if (TicketNum > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(Thread.currentThread().getName() + "买出一张票，还剩: " + --TicketNum);
        }
    }
}