package com.itbys.java_basics._04_thread;

/**
 * Author xx
 * Date 2023/3/14
 * Desc
 */
public class _04_sale_tickets_runnable {

    public static void main(String[] args) {

        Tickets01 tickets01 = new Tickets01();
        Thread thread01 = new Thread(tickets01);
        Thread thread02 = new Thread(tickets01);
        Thread thread03 = new Thread(tickets01);

        thread01.start();
        thread02.start();
        thread03.start();

    }

}


class Tickets01 extends Thread {

    private static int TicketNum = 100;

    @Override
    public void run() {

        while (true) {
            if (TicketNum > 0) {
                System.out.println(currentThread().getName() + "买出一张票，还剩: " + --TicketNum);
            } else
                break;
        }

    }
}