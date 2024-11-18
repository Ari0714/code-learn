package com.itbys.java_basics.chapter07_collection;

import org.junit.Test;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;


/**
 * Author chenjie
 * Date 2024/2/26
 * Desc set
 */
public class _03_set {

    public static void main(String[] args) {

        HashSet hashSet = new HashSet();

        hashSet.add(new Person("Ari", 'm'));
        hashSet.add(123);
        hashSet.add(435L);
        hashSet.add("aabbcc");
        hashSet.add(new Person("Bear", 'f'));
        hashSet.add(new Person("Bear", 'f'));

        for (Object o : hashSet) {
            System.out.println(o);
        }

    }


    @Test
    public void test01() {

        HashSet hashSet = new LinkedHashSet();

        hashSet.add(new Person("Ari", 'm'));
        hashSet.add(123);
        hashSet.add(435L);
        hashSet.add("aabbcc");
        hashSet.add(new Person("Bear", 'f'));
        hashSet.add(new Person("Bear", 'f'));

        for (Object o : hashSet) {
            System.out.println(o);
        }
    }


    @Test
    public void test02() {

        TreeSet<Integer> integers = new TreeSet<>();
        integers.add(-11);
        integers.add(11);
        integers.add(2);
        integers.add(3);
        integers.add(15);

        for (Integer integer : integers) {
            System.out.println(integer);
        }

        TreeSet<Person> integers01 = new TreeSet<>();
        integers01.add(new Person("aa", 'm', 14));
        integers01.add(new Person("aa", 'm', 13));
        integers01.add(new Person("aa", 'm', 11));
        integers01.add(new Person("aa", 'm', 10));
        integers01.add(new Person("aa", 'm', 25));

        for (Person person : integers01) {
            System.out.println(person);
        }


        //
        Comparator<Integer> integerComparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof Person && o2 instanceof Person) {
                    Person person01 = (Person) o1;
                    Person person02 = (Person) o2;
                    return Integer.compare(person01.age, person02.age);
                }

                throw new RuntimeException("输入的类不合法!!!");
            }
        };

        TreeSet<Person> integers02 = new TreeSet(integerComparator);

    }


}


class Person implements Comparable {

    String name;
    char sex;
    int age;


    public Person(String name, char sex, int age) {
        this.name = name;
        this.sex = sex;
        this.age = age;
    }

    public Person(String name, char sex) {
        this.name = name;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (sex != person.sex) return false;
        return name != null ? name.equals(person.name) : person.name == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (int) sex;
        return result;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Person) {
            Person person = (Person) o;
            if (this.age > person.age)
                return 1;
            else if (this.age < person.age)
                return -1;
            else
                return 0;
        }
        throw new RuntimeException("输入的类不合法！！！");
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                '}';
    }
}
