package com.itbys.java_basics_bank._01_banking.domain;

/**
 * Author chenjie
 * Date 2024/2/26
 * Desc
 */
public class WithdrawException extends RuntimeException {

    private Double deficit;

    public Double getDeficit() {
        return deficit;
    }

    public WithdrawException(String message, Double deficit) {
        super(message);
        this.deficit = deficit;
    }

}
