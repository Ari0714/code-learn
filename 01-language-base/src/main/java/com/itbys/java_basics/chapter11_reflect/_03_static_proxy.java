package com.itbys.java_basics.chapter11_reflect;


/**
 * Author chenjie
 * Date 2024/2/26
 * Desc 代理类和被代理类在编译期间就被确定下来
 */
public class _03_static_proxy {

    public static void main(String[] args) {

        PersonX personX = new PersonX();

        HouseProxy houseProxy = new HouseProxy(personX);

        houseProxy.buy();

    }
}


interface House {

    void buy();

}


class HouseProxy implements House {

    private House house;

    public HouseProxy(House house) {
        this.house = house;
    }

    @Override
    public void buy() {

        System.out.println("买房子找我");
        house.buy();

    }
}


class PersonX implements House {

    @Override
    public void buy() {
        System.out.println("我先看看钱够不够");
    }

}
