package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/accounts")
@CrossOrigin(origins = "*")
public class AccountController {

    private AccountDao accountDao;

    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/balance")
    public BigDecimal getBalance(Principal principal) {
        String username = principal.getName();
        Account account = accountDao.findAccountByUsername(username);
        return account.getBalance();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user/{userId}")
    public Account getAccountByUserId(@PathVariable int userId) {
        return accountDao.findAccountByUserId(userId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{accountId}")
    public Account getAccountById(@PathVariable int accountId) {
        return accountDao.findAccountByAccountId(accountId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public List<Account> getAllAccounts() {
        return accountDao.getAllAccounts();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountDao.createAccount(account);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{accountId}")
    public Account updateAccount(@PathVariable int accountId, @RequestBody Account account) {
        return accountDao.updateAccount(accountId, account);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{accountId}")
    public void deleteAccount(@PathVariable int accountId) {
        accountDao.deleteAccount(accountId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/transfer/send")
    public Transfer sendTransfer(@RequestBody Transfer transfer) {
        if (transfer.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransferInvalidException("Transfer amount must be greater than zero.");
        }

        Account senderAccount = accountDao.findAccountByAccountId(transfer.getFromAccountId());
        Account receiverAccount = accountDao.findAccountByAccountId(transfer.getToAccountId());

        if (senderAccount.getUserId() == receiverAccount.getUserId()) {
            throw new TransferInvalidException("Cannot send money to yourself.");
        }

        if (senderAccount.getBalance().compareTo(transfer.getAmount()) < 0) {
            throw new TransferInvalidException("Insufficient funds to complete the transfer.");
        }

        accountDao.subtractFromBalance(transfer.getFromAccountId(), transfer.getAmount());
        accountDao.addToBalance(transfer.getToAccountId(), transfer.getAmount());

        return transfer;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/transfers")
    public List<Transfer> getTransfers(Principal principal) {
        String username = principal.getName();
        Account account = accountDao.findAccountByUsername(username);
        return accountDao.getTransfersByAccountId(account.getAccountId());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/transfers/{transferId}")
    public Transfer getTransferById(@PathVariable int transferId) {
        return accountDao.getTransferByTransferId(transferId);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TransferInvalidException.class)
    public void handleTransferInvalidException(TransferInvalidException ex) {
        // Log the error message
        System.err.println("Transfer Invalid: " + ex.getMessage());
    }

    // Custom Exception class for TransferInvalidException
    private static class TransferInvalidException extends RuntimeException {
        public TransferInvalidException(String message) {
            super(message);
        }
    }
}
