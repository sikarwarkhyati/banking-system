package com.banking.accountservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.banking.accountservice.dto.AccountResponse;
import com.banking.accountservice.dto.CreateAccountRequest;
import com.banking.accountservice.entity.AccountStatus;
import com.banking.accountservice.entity.AccountType;
import com.banking.accountservice.exception.AccountNotActiveException;
import com.banking.accountservice.exception.AccountNotFoundException;
import com.banking.accountservice.exception.DuplicateAccountException;
import com.banking.accountservice.exception.InsufficientBalanceException;
import com.banking.accountservice.service.AccountService;

@SpringBootTest
class AccountServiceTests {

    @Autowired
    private AccountService accountService;

    private AccountResponse newAccount(String deposit) {
        String email = UUID.randomUUID() + "@test.com";
        return accountService.createAccount(new CreateAccountRequest(
                "Test User", email, "9999999999", AccountType.SAVINGS, new BigDecimal(deposit)));
    }

    @Test
    void createsActiveAccountWithTenDigitNumber() {
        AccountResponse account = newAccount("500");
        assertEquals(AccountStatus.ACTIVE, account.getAccountStatus());
        assertEquals(10, account.getAccountNumber().length());
        assertEquals(0, new BigDecimal("1000.00").compareTo(account.getDailyTransactionLimit()));
    }

    @Test
    void rejectsDuplicateEmail() {
        CreateAccountRequest request = new CreateAccountRequest(
                "A", "dup@test.com", "1", AccountType.CURRENT, BigDecimal.TEN);
        accountService.createAccount(request);
        assertThrows(DuplicateAccountException.class, () -> accountService.createAccount(request));
    }

    @Test
    void deductAndCreditUpdateBalance() {
        String number = newAccount("500").getAccountNumber();
        accountService.deductBalance(number, new BigDecimal("200"));
        accountService.creditBalance(number, new BigDecimal("50"));
        assertEquals(0, new BigDecimal("350").compareTo(accountService.getBalance(number)));
    }

    @Test
    void deductFailsWhenBalanceTooLow() {
        String number = newAccount("100").getAccountNumber();
        assertThrows(InsufficientBalanceException.class,
                () -> accountService.deductBalance(number, new BigDecimal("101")));
    }

    @Test
    void blockedAccountCannotBeDebited() {
        String number = newAccount("100").getAccountNumber();
        accountService.blockAccount(number);
        assertThrows(AccountNotActiveException.class,
                () -> accountService.deductBalance(number, BigDecimal.ONE));
    }

    @Test
    void rejectsNonPositiveAmountsAndUnknownAccounts() {
        String number = newAccount("100").getAccountNumber();
        assertThrows(IllegalArgumentException.class,
                () -> accountService.creditBalance(number, BigDecimal.ZERO));
        assertThrows(AccountNotFoundException.class, () -> accountService.getAccount("0000000000"));
    }

}
