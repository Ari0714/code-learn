package com.itbys.java_basics.chapter04_thread;

/**
 * Author xx
 * Date 2023/3/14
 * Desc dead lock case
 */
public class _07_dead_lock {

    public static void main(String[] args) {

        StringBuffer str01 = new StringBuffer();
        StringBuffer str02 = new StringBuffer();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (str02){
                    str01.append("a");
                    str02.append("1");

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    synchronized (str01) {
                        str01.append("b");
                        str02.append("2");

                        System.out.println(str01);
                        System.out.println(str02);
                    }
                }
            }
        }).start();



        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (str01){
                    str01.append("c");
                    str02.append("3");

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    synchronized (str02) {
                        str01.append("d");
                        str02.append("4");

                        System.out.println(str01);
                        System.out.println(str02);

                    }
                }
            }
        }).start();

    }

}
