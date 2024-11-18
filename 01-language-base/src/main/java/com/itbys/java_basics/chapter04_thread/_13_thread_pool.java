package com.itbys.java_basics.chapter04_thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class _13_thread_pool {

    public static void main(String[] args) {

        //多态
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        //设置属性
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService;


        executorService.execute(new AA());
        executorService.execute(new AA());

        executorService.shutdown();

    }

}



class AA implements Runnable{

    @Override
    public void run() {

        for (int i = 1; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() +": "+ i);
        }
    }
}