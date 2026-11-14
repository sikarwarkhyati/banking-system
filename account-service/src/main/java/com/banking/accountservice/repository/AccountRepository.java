package com.banking.accountservice.repository;

import java.util.Optional;

import com.banking.accountservice.entity.Account;

public interface AccountRepository extends jpa.repository.JpaRepository<Account, String> {

    boolean existsByEmail(String email);

    boolean existsByAccountNumber(String accountNumber);

    Optional<Account> findByAccountNumber(String accountNumber);

    Optional<Account> findByAccountNumberAndAccountStatus(String accountNumber, AccountStatus accountStatus);

}
