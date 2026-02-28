package com.itbys.java_basics._02_oop;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author Ari
 * Date 2023/3/14
 * Desc
 */
public class SalarySystem {

    public static void main(String[] args) {

        EmployeeSalary coco = new EmployeeSalary("coco", 20, new Mydate("2020", "02", "01"), 3000);
        System.out.println(coco.earnings());

    }
}


class EmployeeSalary extends employee {

    private int monthlySslary;

    public EmployeeSalary(String name, int number, Mydate birth, int monthlySslary) {
        super(name, number, birth);
        this.monthlySslary = monthlySslary;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int earnings() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(new Date());
        String month = format.substring(5, 7);
        if (month.equals(super.getMydate().getMonth()))
            return this.monthlySslary += 100;
        return this.monthlySslary;
    }

    public int getMonthlySslary() {
        return monthlySslary;
    }

    public void setMonthlySslary(int monthlySslary) {
        this.monthlySslary = monthlySslary;
    }

}


abstract class employee {

    private String name;
    private int number;
    private Mydate birth;

    public employee(String name, int number, Mydate birth) {
        this.name = name;
        this.number = number;
        this.birth = birth;
    }

    public abstract int earnings();

    @Override
    public String toString() {
        return name + "_" + number + "_" + birth;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Mydate getMydate() {
        return birth;
    }

    public void setMydate(Mydate mydate) {
        this.birth = mydate;
    }
}


class Mydate {

    private String year;
    private String month;
    private String day;

    public Mydate(String year, String month, String day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    public String toString() {
        return year + "年" + month + "月" + day;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}





