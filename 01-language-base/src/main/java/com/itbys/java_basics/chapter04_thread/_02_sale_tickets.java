package com.itbys.java_basics.chapter04_thread;

/**
 * Author xx
 * Date 2023/3/14
 * Desc
 */
public class _02_sale_tickets {

    public static void main(String[] args) {

        Tickets tickets01 = new Tickets();
        Tickets tickets02 = new Tickets();
        Tickets tickets03 = new Tickets();

        tickets01.start();
        tickets02.start();
        tickets03.start();

    }

}


class Tickets extends Thread {

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