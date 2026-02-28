package com.itbys.java_basics._11_reflect;

public class _02_getclazz_method {


    public static void main(String[] args) throws ClassNotFoundException {

        //获取Clazz实例的方式

        //
        Class<Person> clazz01 = Person.class;

        //
        Person person = new Person("coco", 12);
        Class<? extends Person> clazz02 = person.getClass();

        //
        Class<?> clazz03 = Class.forName("com.itbys.java_basics._11_reflect.Person");

        //类的加载器
        ClassLoader classLoader = Person.class.getClassLoader();
        Class<?> clazz04 = classLoader.loadClass("com.itbys.java_basics._11_reflect.Person");

        System.out.println(clazz01);
        System.out.println(clazz02);
        System.out.println(clazz03);
        System.out.println(clazz04);


    }

}



