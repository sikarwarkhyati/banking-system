package com.banking.accountservice.exception;

public class DuplicateAccountException extends RuntimeException {

    public DuplicateAccountException(String email) {
        super("Account with email already exists: " + email);
    }

}
