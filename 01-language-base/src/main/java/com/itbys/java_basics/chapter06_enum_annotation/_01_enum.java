package com.itbys.java_basics.chapter06_enum_annotation;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class _01_enum {

    public static void main(String[] args) {

        Status free = Status.FREE;

        Season summer = Season.SUMMER;
        System.out.println(summer);

        Season[] values = Season.values();

        Season spring = Season.valueOf("SPRING");
        System.out.println(spring);

    }

}


enum Season {

    SPRING("春天", "春乱花开"),
    SUMMER("夏天", "烈日炎炎"),
    AUTUMN("秋天", "秋高气爽"),
    WINTER("冬天", "冰天雪地");

    private final String name;
    private final String desc;

    private Season(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }


    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "Season{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }

}


class Status {

    private final String NAME;

    public Status(String NAME) {
        this.NAME = NAME;
    }

    public static final Status FREE = new Status("FREE");
    public static final Status BUSY = new Status("BUSY");
    public static final Status VOCATION = new Status("VOCATION");

    public String getNAME() {
        return NAME;
    }

    public static Status getFREE() {
        return FREE;
    }

    public static Status getBUSY() {
        return BUSY;
    }

    public static Status getVOCATION() {
        return VOCATION;
    }
}
