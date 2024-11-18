package com.itbys.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class _06_callable {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        FutureTask futureTask = new FutureTask(new CC());

        Thread thread = new Thread(futureTask);

        thread.start();

        System.out.println(futureTask.get());
    }

}


class CC implements Callable {

    @Override
    public Object call() throws Exception {
        return 111;
    }
}