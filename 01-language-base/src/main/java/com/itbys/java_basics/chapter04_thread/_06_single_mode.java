package com.itbys.java_basics.chapter04_thread;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _06_single_mode {

    public static void main(String[] args) {

        Singleton2 singleton = Singleton2.getSingleton();
        Singleton2 singleton2 = Singleton2.getSingleton();
        System.out.println(singleton.hashCode());
        System.out.println(singleton2.hashCode());

    }

}


// 饿汉
class Singleton {

    private static Singleton instance = new Singleton();

    private Singleton() {
    }

    public static Singleton getSingleton() {
        return instance;
    }
}


// 懒汉
class Singleton2 {

    private static Singleton2 instance;

    private Singleton2() {
    }

    public static Singleton2 getSingleton() {
        if (instance == null)
            instance = new Singleton2();
        return instance;

    }
}

// 懒汉优化
class Single {

    private Single() {
    }

    private static Single single;

    //一
    public static synchronized Single getInstance() {
        if (single == null)
            single = new Single();
        return single;
    }

    //二
    public static Single getInstance02() {
        synchronized (Single.class) {
            if (single == null)
                single = new Single();
            return single;
        }
    }

    //三  优化
    public static Single getInstance03() {
        if (single == null) {
            synchronized (Single.class) {
                if (single == null)
                    single = new Single();
            }
        }

        return single;
    }

}




