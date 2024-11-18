package com.itbys.java_basics_bank._01_banking;

import java.util.ArrayList;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class Bank {

    private ArrayList<Customer> customers;
    private static int numberOfCustomers;

    private Bank(ArrayList<Customer> customers, int numberOfCustomers) {
        this.customers = customers;
        this.numberOfCustomers = numberOfCustomers;
    }

    private static Bank bank = null;

    public static synchronized Bank getBanking() {
        if (bank == null)
            bank = new Bank(new ArrayList<>(), 0);
        return bank;
    }


    public void addCustomer(String f, String l) {
        Customer customer = new Customer(f, l);
        customers.add(customer);
        numberOfCustomers += 1;
    }

    public int getNumOfCustomers() {
        return numberOfCustomers;
    }

    public Customer getCustomer(int i) {
        return customers.get(i);
    }


    @Override
    public String toString() {
        return "Bank{" +
                "customers=" + customers +
                '}';
    }
}


