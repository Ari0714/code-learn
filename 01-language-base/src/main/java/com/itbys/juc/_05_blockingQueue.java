package com.itbys.juc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class _05_blockingQueue {

    public static void main(String[] args) throws InterruptedException {

        ArrayBlockingQueue<String> strings = new ArrayBlockingQueue<String>(3);

        //exception
        strings.add("a");
        strings.add("b");
        strings.add("c");
        strings.add("d");
        //先进先出
        strings.remove("a");
        //检查对首元素
        String element = strings.element();

        //特殊值false  null  a
        strings.offer("a");
        strings.poll();
        strings.peek();

        //put take  没数据堵住 死战不退
        strings.put("aa");
        strings.take();

        //超时
        strings.offer("qq", 1000L, TimeUnit.SECONDS);
        strings.poll(1000L, TimeUnit.SECONDS);


    }

}
