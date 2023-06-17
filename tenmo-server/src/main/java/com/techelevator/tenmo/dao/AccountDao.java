package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {
    BigDecimal getBalanceByUserId(int userId);
    BigDecimal getBalanceByAccountId(int accountId);
    BigDecimal getBalance(String username);

    void addToBalance(int accountId, BigDecimal amount);
    void subtractFromBalance(int accountId, BigDecimal amount);
    Account findAccountByUserId(int userId);
    Account findAccountByAccountId(int accountId);
    Account findAccountByUsername(String username);

    void deleteAccount(int accountId);

    Account updateAccount(int accountId, Account account);

    Account createAccount(Account account);

    List<Account> getAllAccounts();

    List<Transfer> getTransfersByAccountId(int accountId);

    Transfer getTransferByTransferId(int transferId);

    Transfer sendTransfer(Transfer transfer);
}


