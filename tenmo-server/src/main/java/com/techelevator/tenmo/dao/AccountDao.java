package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
public interface AccountDao {
    BigDecimal getBalance(int userId);

    BigDecimal getBalanceByAccountId(int accountId);

    BigDecimal addToBalance(BigDecimal amountToAdd, int id);

    BigDecimal subtractFromBalance(BigDecimal amountToSubtract, int id);

    Account findAccountByUserId(int userId);

    public Account findAccountByAccountId(int id);
}
