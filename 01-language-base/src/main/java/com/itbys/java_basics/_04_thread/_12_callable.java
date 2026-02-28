package com.itbys.java_basics._04_thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _12_callable {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Mycall mycall = new Mycall();

        FutureTask futureTask = new FutureTask(mycall);

        new Thread(futureTask).start();

        System.out.println(futureTask.get());

    }
}


class Mycall implements Callable {

    private static int sum;

    @Override
    public Object call() throws Exception {

        for (int i = 0; i < 101; i++) {
            sum += i;
        }

        return sum;
    }
}