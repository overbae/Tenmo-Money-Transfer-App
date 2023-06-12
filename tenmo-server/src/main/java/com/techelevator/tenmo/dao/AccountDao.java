package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
public interface AccountDao {
    BigDecimal getBalanceByUserId(int userId);

    BigDecimal getBalance(int userId);

    BigDecimal getBalanceByAccountId(int accountId);

    BigDecimal addToBalance(BigDecimal amountToAdd, int id);

    BigDecimal subtractFromBalance(BigDecimal amountToSubtract, int id);

    void addToBalance(int accountId, BigDecimal amount);

    void subtractFromBalance(int accountId, BigDecimal amount);

    Account findAccountByUserId(int userId);

    Account findAccountByAccountId(int id);

    Account findAccountByUsername(String username);
}
