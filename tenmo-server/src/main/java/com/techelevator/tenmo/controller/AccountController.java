package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
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
    private final AccountDao accountDao;

    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    // Handles GET request to retrieve all accounts for a specific user
    @GetMapping("/accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<Account> getAccounts(Principal principal) {
        // Retrieve accounts associated with the username obtained from Principal
        List<Account> accounts = accountDao.findByUsername(principal.getName());

        if (accounts != null) {
            // Return the list of accounts if it's not null
            return accounts;
        }
        // Throw an exception with status code 404 (NOT_FOUND) and message "Account Not Found"
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Not Found");
    }

    // Handles GET request to retrieve an account by user ID
    @GetMapping("/account/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account getAccountByUserId(@PathVariable int id) {
        // Retrieve the account associated with the given user ID
        Account account = accountDao.findByUserId(id);

        if (account != null) {
            // Return the account if it's not null
            return account;
        }
        // Throw an exception with status code 404 (NOT_FOUND) and message "Account Not Found"
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Not Found");
    }

    // Handles GET request to retrieve an account by account ID
    @GetMapping("/get_account/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Account getAccountByAccountId(@PathVariable int id) {
        // Retrieve the account associated with the given account ID
        Account account = accountDao.getAccountById(id);

        if (account != null) {
            return account;
        }
        // Throw an exception with status code 404 (NOT_FOUND) and message "Account Not Found"
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account Not Found");
    }

    // Handles PUT request to update an account
    @PutMapping("/account")
    public ResponseEntity<Boolean> updateAccount(@RequestBody @Valid Account account) {
        if (accountDao.update(account)) {
            // If the account update is successful, return a ResponseEntity with status code 200 (OK)
            return ResponseEntity.ok().build();
        }
        // If the account update fails, return a ResponseEntity with status code 404 (NOT_FOUND)
        return ResponseEntity.notFound().build();
    }
}
//comment for push