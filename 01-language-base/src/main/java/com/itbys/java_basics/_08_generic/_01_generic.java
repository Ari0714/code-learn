package com.itbys.java_basics._08_generic;

/**
 * Author Ari
 * Date 2024/2/26
 * Desc 泛型
 */
public class _01_generic {

    public static void main(String[] args) {

        //自定义泛型
        Peson<String> stringPeson = new Peson<>();
        stringPeson.setGenericT("aa");

    }

}


class Son extends Peson<String> {

}

class Son01<T> extends Peson<T> {

}


class Peson<T> {
    String name;
    short age;

    T genericT;

    public Peson(String name, short age, T genericT) {
        this.name = name;
        this.age = age;
        this.genericT = genericT;
    }

    public Peson() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getAge() {
        return age;
    }

    public void setAge(short age) {
        this.age = age;
    }

    public T getGenericT() {
        return genericT;
    }

    public void setGenericT(T genericT) {
        this.genericT = genericT;
    }
}