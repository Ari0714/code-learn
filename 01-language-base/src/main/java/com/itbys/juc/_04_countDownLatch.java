package com.itbys.juc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class _04_countDownLatch {

    public static void main(String[] args) throws InterruptedException {

        //倒计时
        CountDownLatch countDownLatch = new CountDownLatch(5);
        //收集龙珠
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> {
            System.out.println("召唤神龙");
        });
        //6个车抢3个停车位
        Semaphore semaphore = new Semaphore(6);

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                System.out.println("走了");
                countDownLatch.countDown();
            }, String.valueOf(i)).run();
        }

        countDownLatch.await();

        System.out.println("门锁了");

    }

}
