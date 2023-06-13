package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.dao.AccountDao;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/account")
@CrossOrigin(origins = "*")
public class AccountController {

    private final AccountDao accountDao;

    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user/{userId}/balance")
    public BigDecimal getBalance(@PathVariable int userId) {
        return accountDao.getBalance(userId);
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user/{userId}")
    public Account findAccountByUserId(@PathVariable int userId) {
        return accountDao.findAccountByUserId(userId);
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{accountId}")
    public Account findAccountById(@PathVariable int accountId) {
        return accountDao.findAccountByAccountId(accountId);
    }
}
