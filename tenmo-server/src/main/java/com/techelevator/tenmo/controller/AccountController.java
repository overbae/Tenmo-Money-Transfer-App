package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
@PreAuthorize("isAuthenticated()")
public class AccountController {
    private AccountDao accountDao;

    // Constructor to initialize the AccountController with an AccountDao
    public AccountController(JdbcAccountDao accountDao) {
        this.accountDao = accountDao;
    }

    // Handles GET request to retrieve all accounts for a specific user
    @GetMapping("/accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getAccounts(Principal principal) {
        List<Account> accounts = accountDao.findByUsername(principal.getName());

        if (accounts != null) {
            return accounts;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Not Found");
    }

    // Handles GET request to retrieve an account by user ID
    @GetMapping("/account/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account getAccountByUserId(@PathVariable int id) {
        Account account = accountDao.findByUserId(id);

        if (account != null) {
            return account;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Not Found");
    }

    // Handles GET request to retrieve an account by account ID
    @GetMapping("/get_account/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account getAccountByAccountId(@PathVariable int id) {
        Account account = accountDao.getAccountById(id);

        if (account != null) {
            return account;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Not Found");
    }

    // Handles PUT request to update an account
    @PutMapping("/account")
    public ResponseEntity<Boolean> update(@RequestBody @Valid Account account) {
        if (accountDao.update(account)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
