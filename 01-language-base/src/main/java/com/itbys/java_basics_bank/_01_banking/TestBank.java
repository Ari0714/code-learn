package com.itbys.java_basics_bank._01_banking;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class TestBank {

    public static void main(String[] args) {

        //
        Account account = new Account(60.0);
        System.out.println(account.getBalance());

        account.deposit(30.0);
        System.out.println(account.getBalance());

        account.withdraw(50.0);
        System.out.println(account.getBalance());

        //
        Customer customer = new Customer("chen", "j");
        customer.setAccount(account);
        System.out.println(customer.toString());

        //
        System.out.println(account.deposit(20.0));
        account.withdraw(30.0);

        //
        Bank bank = Bank.getBanking();
        bank.addCustomer("chen", "j");
        bank.addCustomer("chen02", "j");
        bank.addCustomer("chej03", "j");
        bank.addCustomer("chen04", "j");

        System.out.println(bank.getNumOfCustomers());
        System.out.println(bank.getCustomer(1));
        System.out.println(bank.toString());

        //
        CheckingAccount checkingAccount = new CheckingAccount(200.0, 200.0);
        checkingAccount.withdraw(401.0);


    }
}
