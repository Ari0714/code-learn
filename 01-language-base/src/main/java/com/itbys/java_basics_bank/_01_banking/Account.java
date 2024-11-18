package com.itbys.java_basics_bank._01_banking;

import com.itbys.java_basics_bank._01_banking.domain.WithdrawException;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class Account {

    protected Double balance;

    public Account(Double balance) {
        this.balance = balance;
    }

    public Double getBalance() {
        return balance;
    }

    public Boolean deposit(Double balance) {
        this.balance += balance;
        return true;
    }

    public void withdraw(Double balance) {
        if (this.balance >= balance) {
            this.balance -= balance;
        } else
            System.out.println("余额不足");
    }

    @Override
    public String toString() {
        return "Account{" +
                "balance=" + balance +
                '}';
    }
}

class SavingAccount extends Account {

    private Double interestRate;

    public SavingAccount(Double balance, Double interestRate) {
        super(balance);
        this.interestRate = interestRate;
    }

}


class CheckingAccount extends Account {

    private Double overdrawProtection;

    public CheckingAccount(Double balance) {
        super(balance);
    }

    public CheckingAccount(Double balance, Double overdrawProtection) {
        super(balance);
        this.overdrawProtection = overdrawProtection;
    }

    @Override
    public void withdraw(Double balance) {

        if (super.balance < balance) {
            throw new WithdrawException("no overdraft protection", 200.0);
        }

        if (super.balance + overdrawProtection < balance) {
            throw new WithdrawException("Insufficient funds for overdraft protection", 200.0);

        }
    }
}
