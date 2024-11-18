package com.itbys.jvm._01_mem_gc._02_chapter_class_loader;

import org.junit.Test;

public class _01_class_load {

    @Test
    public void test01() {

        //sun.misc.Launcher$AppClassLoader@18b4aac2
        ClassLoader classLoader = _01_class_load.class.getClassLoader();
        System.out.println(classLoader);

        //sun.misc.Launcher$ExtClassLoader@452b3a41
        ClassLoader parent = classLoader.getParent();
        System.out.println(parent);

        //null
        ClassLoader parent1 = parent.getParent();
        System.out.println(parent1);

        //sun.misc.Launcher$AppClassLoader@18b4aac2
        ClassLoader classLoader1 = String.class.getClassLoader();
        System.out.println(classLoader1);


    }

}
