package com.itbys.java_basics._04_thread;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _09_test {

    public static void main(String[] args) {

        Account account = new Account();

        Thread thread01 = new Thread(account);
        Thread thread02 = new Thread(account);
        Thread thread03 = new Thread(account);

        thread01.start();
        thread02.start();
        thread03.start();
    }

}

class Account implements Runnable {

    private static int money;

    @Override
    public void run() {

        synchronized (this) {
            for (int i = 0; i < 3; i++) {
                money += 1000;
                System.out.println(money);
            }
        }

    }
}
