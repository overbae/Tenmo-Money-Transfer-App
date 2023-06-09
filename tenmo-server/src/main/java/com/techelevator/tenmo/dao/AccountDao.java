package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.model.AccountDto;

import java.math.BigDecimal;
public interface AccountDao {
    BigDecimal getBalance(int userId);

    BigDecimal getBalanceByAccountId(int accountId);

    BigDecimal addToBalance(BigDecimal amountToAdd, int id);

    BigDecimal subtractFromBalance(BigDecimal amountToSubtract, int id);

    AccountDto findAccountByUserId(int userId);

    public AccountDto findAccountByAccountId(int id);
}
