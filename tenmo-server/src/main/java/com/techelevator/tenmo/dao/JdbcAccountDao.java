package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;

@Repository
public class JdbcAccountDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalanceByUserId(int userId) {
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
    }

    @Override
    public BigDecimal getBalance(int userId) {
        return null;
    }

    @Override
    public BigDecimal getBalanceByAccountId(int accountId) {
        String sql = "SELECT balance FROM account WHERE account_id = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
    }

    @Override
    public BigDecimal addToBalance(BigDecimal amountToAdd, int id) {
        return null;
    }

    @Override
    public BigDecimal subtractFromBalance(BigDecimal amountToSubtract, int id) {
        return null;
    }

    @Override
    public void addToBalance(int accountId, BigDecimal amount) {
        String sql = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
        jdbcTemplate.update(sql, amount, accountId);
    }

    @Override
    public void subtractFromBalance(int accountId, BigDecimal amount) {
        String sql = "UPDATE account SET balance = balance - ? WHERE account_id = ?";
        jdbcTemplate.update(sql, amount, accountId);
    }

    @Override
    public Account findAccountByUserId(int userId) {
        String sql = "SELECT * FROM account WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToAccount, userId);
    }


    @Override
    public Account findAccountByAccountId(int accountId) {
        String sql = "SELECT * FROM account WHERE account_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToAccount, accountId);
    }

    @Override
    public Account findAccountByUsername(String username) {
        String sql = "SELECT a.* FROM account a JOIN tenmo_user u ON a.user_id = u.user_id WHERE u.username = ?";

        return jdbcTemplate.queryForObject(sql, this::mapRowToAccount, username);
    }


    private Account mapRowToAccount(ResultSet rs, int rowNum) throws SQLException {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
}
