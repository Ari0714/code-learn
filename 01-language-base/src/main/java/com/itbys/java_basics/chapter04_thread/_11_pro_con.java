package com.itbys.java_basics.chapter04_thread;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _11_pro_con {

    public static void main(String[] args) {

        Clerk clerk = new Clerk();

        ProduceProduct produceProduct = new ProduceProduct(clerk);
        ConsumeProduct consumeProduct = new ConsumeProduct(clerk);

        Thread thread = new Thread(produceProduct);
        Thread thread1 = new Thread(consumeProduct);

        thread.start();
        thread1.start();

    }

}


class Clerk {

    private static int cnt = 0;

    public synchronized void pro() {
        if (cnt < 20) {
            cnt++;
            System.out.println(Thread.currentThread().getName() + "生产，当前数量：" + cnt);
            notify();
        } else {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void con() {
        if (cnt > 0) {
            cnt--;
            System.out.println(Thread.currentThread().getName() + "消费，当前数量：" + cnt);
            notify();
        } else {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

class ProduceProduct implements Runnable {

    private Clerk clerk;

    public ProduceProduct(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        while (true)
            clerk.pro();
    }
}


class ConsumeProduct implements Runnable {

    private Clerk clerk;

    public ConsumeProduct(Clerk clerk) {
        this.clerk = clerk;
    }

    @Override
    public void run() {
        while (true)
            clerk.con();
    }
}
