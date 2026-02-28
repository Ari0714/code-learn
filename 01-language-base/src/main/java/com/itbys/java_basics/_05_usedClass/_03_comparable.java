package com.itbys.java_basics._05_usedClass;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _03_comparable {

    public static void main(String[] args) {

        //
        String[] str = new String[]{"bb", "dd", "gg", "ll", "qq", "kk"};
        Arrays.sort(str);
        System.out.println(Arrays.toString(str));

        //
        Person person = new Person("aa", 12);
        Person person1 = new Person("bb", 15);

        System.out.println(person.compareTo(person1));

        //comparator
        Arrays.sort(str, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return -o1.compareTo(o2);
            }
        });

        System.out.println(Arrays.toString(str));

        String aa = "2345356463423423432548867547456456546365646";

        BigInteger bigInteger = new BigInteger(aa);
        System.out.println(bigInteger);


    }
}


class Person implements Comparable {

    String name;
    int age;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }


    @Override
    public int compareTo(Object o) {
        if (o instanceof Person) {
            Person person = (Person) o;
            if (this.age > person.age)
                return 1;
            else if (this.age < person.age)
                return -1;
            else {
//                return 0;
                return this.name.compareTo(person.name);
            }
        }

        throw new RuntimeException("输入数据类型不对");
    }


}