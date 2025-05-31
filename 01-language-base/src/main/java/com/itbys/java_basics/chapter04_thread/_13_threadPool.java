package com.itbys.java_basics.chapter04_thread;

import java.util.concurrent.*;

/**
 * Author Ari
 * Date 2025/5/28
 * Desc
 */
public class _13_threadPool {

    public static void main(String[] args) {


        /**
         * 4 ThreadPool
         */
        //1. fixed
        //特点：只有核心线程，线程数量固定，执行完立即回收，任务队列为链表结构的有界队列。
        //应用场景：控制线程最大并发数
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("implement task！！！");
            }
        };
        executorService.execute(runnable);

        //        executorService.execute(new AA());
        //        executorService.execute(new AA());
        //        executorService.shutdown();

        //2. scheduled
        //特点：核心线程数量固定，非核心线程数量无限，执行完闲置 10ms 后回收，任务队列为延时阻塞队列。
        //应用场景：执行定时或周期性的任务。
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("implement task2！！！");
            }
        };
        scheduledExecutorService.schedule(runnable1, 10, TimeUnit.SECONDS);
        scheduledExecutorService.scheduleAtFixedRate(runnable1, 20, 1000, TimeUnit.SECONDS);

        //3. cached
        //特点：无核心线程，非核心线程数量无限，执行完闲置 60s 后回收，任务队列为不存储元素的阻塞队列。
        //应用场景：执行大量、耗时少的任务
        ExecutorService cacheExecutorService1 = Executors.newCachedThreadPool();
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                System.out.println("implement task3！！！");
            }
        };
        cacheExecutorService1.execute(runnable2);

        //4. single
        //特点：只有 1 个核心线程，无非核心线程，执行完立即回收，任务队列为链表结构的有界队列。
        //应用场景：不适合并发但可能引起 IO 阻塞性及影响 UI 线程响应的操作，如数据库操作、文件操作等。
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        Runnable runnable3 = new Runnable() {
            @Override
            public void run() {
                System.out.println("implement task4！！！");
            }
        };
        singleThreadExecutor.execute(runnable);


        /**
         * ThreadPoolExecutor
         * corePoolSize 线程池核心线程大小
         * maximumPoolSize 线程池最大线程数量
         * keepAliveTime 空闲线程存活时间
         * unit 空闲线程存活时间单位
         * LinkedBlockingQueue：基于链表的无界阻塞队列（其实最大容量为Interger.MAX），按照FIFO排序
         * handler 拒绝策略（超过最大任务）：AbortPolicy，该策略下，直接丢弃任务，并抛出RejectedExecutionException异常。
         */
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                2,
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


class AA implements Runnable {

    @Override
    public void run() {

        for (int i = 1; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + ": " + i);
        }
    }
}