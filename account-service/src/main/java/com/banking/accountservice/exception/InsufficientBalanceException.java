package com.banking.accountservice.exception;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(String accountNumber) {
        super("Insufficient balance in account number: " + accountNumber);
    }

}
