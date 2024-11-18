package com.itbys.java_basics.chapter11_reflect;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _01_reflect_new {

    public static void main(String[] args) {

        Person person = new Person("coco", 12);
        System.out.println(person);

    }


    @Test
    public void test02() throws Exception {

        Class<Person> personClass = Person.class;

        Constructor<Person> constructor = personClass.getConstructor(String.class, int.class);
        Person person = constructor.newInstance("coco", 12);
        System.out.println(person);

        Field age = personClass.getDeclaredField("age");
        age.set(person, 22);
        System.out.println(person);

        Method show = personClass.getDeclaredMethod("show");
        show.invoke(person);

        System.out.println("*********************************");


        // 私有
        // 必须调getDeclaredConstructor 带declared
        Constructor<Person> constructor1 = personClass.getDeclaredConstructor(String.class);
        constructor1.setAccessible(true);
        Person person1 = constructor1.newInstance("xiaomei");
        System.out.println(person1);

        Field name = personClass.getDeclaredField("name");
        name.setAccessible(true);
        person1.setName("meimei");
        System.out.println(person1);

        Method eat = personClass.getDeclaredMethod("eat");
        eat.setAccessible(true);
        eat.invoke(person1);

    }

}


class Person {

    private String name;
    public int age;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    private Person(String name) {
        this.name = name;
    }

    public void show() {
        System.out.println("我是public方法");
    }

    private void eat() {
        System.out.println("我是private方法");
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}