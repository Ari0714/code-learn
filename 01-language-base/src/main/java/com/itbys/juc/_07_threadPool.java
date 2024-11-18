package com.itbys.juc;

import java.util.concurrent.*;

public class _07_threadPool {

    public static void main(String[] args) {

        //固定  一个  n个（看情况）  都是ThreaPoolExecutor构造器的对象
        ExecutorService executorService = Executors.newFixedThreadPool(6);
        ExecutorService executorService1 = Executors.newSingleThreadExecutor();
        ExecutorService executorService2 = Executors.newCachedThreadPool();

        //手动创建指定阻塞队列
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,
                5,
                5,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<Runnable>(10),
                new ThreadPoolExecutor.AbortPolicy());


        //线程池中6个线程抢占runnable接口任务
        try {
            for (int i = 0; i < 10; i++) {
                threadPoolExecutor.execute(() -> {
                    System.out.println(Thread.currentThread().getName());
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPoolExecutor.shutdown();
        }

    }

}
