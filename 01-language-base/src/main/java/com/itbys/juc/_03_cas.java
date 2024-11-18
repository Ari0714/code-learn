package com.itbys.juc;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class _03_cas {

    @Test
    public void test01() {

        AtomicInteger atomicInteger = new AtomicInteger(5);

        atomicInteger.compareAndSet(5, 10);
        atomicInteger.compareAndSet(5, 20);

        System.out.println(atomicInteger.get());

    }

}
