package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.techelevator.tenmo.dao.JdbcAccountDao;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private AccountDao accountDao;
    private Object jdbcAccountDao;

    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @GetMapping("/balance")
    public BigDecimal getBalance(Principal principal) {
        String username = principal.getName();
        Account account = accountDao.findAccountByUsername(username);
        return account.getBalance();
    }


    @GetMapping("/{accountId}")
    public Account getAccountById(@PathVariable int accountId) {
        return accountDao.findAccountByAccountId(accountId);
    }

}
