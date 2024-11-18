package com.itbys.juc;


public class _02_single_mode {

    public static void main(String[] args) {

//        Per single = Per.getSingle();

        for (int i = 1; i < 11; i++) {
            new Thread(() -> {
                Per.getSingle02();
            }, String.valueOf(i)).start();

        }

    }
}


class Per {

    private static Per per = null;

    private Per() {
    }

    {
        System.out.println(Thread.currentThread().getName());
    }

    public static synchronized Per getSingle() {
        if (per == null)
            per = new Per();
        return per;
    }

    //优化 DCL 双端检索机制
    public static Per getSingle02() {

        if (per == null) {
            synchronized (Per.class) {
                if (per == null)
                    per = new Per();
            }
        }
        return per;
    }

}