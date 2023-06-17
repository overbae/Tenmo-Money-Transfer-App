package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
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
    public BigDecimal getBalanceByAccountId(int accountId) {
        String sql = "SELECT balance FROM account WHERE account_id = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
    }

    @Override
    public BigDecimal getBalance(String username) {
        String sql = "SELECT balance FROM account WHERE user_id = (SELECT user_id FROM tenmo_user WHERE username = ?)";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, username);
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

    @Override
    public void deleteAccount(int accountId) {
        String sql = "DELETE FROM account WHERE account_id = ?";
        jdbcTemplate.update(sql, accountId);
    }

    @Override
    public Account updateAccount(int accountId, Account account) {
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?";
        jdbcTemplate.update(sql, account.getBalance(), accountId);
        return account;
    }

    @Override
    public Account createAccount(Account account) {
        String sql = "INSERT INTO account (user_id, balance) VALUES (?, ?) RETURNING account_id";
        int newAccountId = jdbcTemplate.queryForObject(sql, Integer.class, account.getUserId(), account.getBalance());
        account.setAccountId(newAccountId);
        return account;
    }

    @Override
    public List<Account> getAllAccounts() {
        String sql = "SELECT * FROM account";
        return jdbcTemplate.query(sql, this::mapRowToAccount);
    }

    @Override
    public List<Transfer> getTransfersByAccountId(int accountId) {
        String sql = "SELECT * FROM transfer WHERE account_from = ? OR account_to = ?";
        return jdbcTemplate.query(sql, this::mapRowToTransfer, accountId, accountId);
    }

    @Override
    public Transfer getTransferByTransferId(int transferId) {
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToTransfer, transferId);
    }

    @Override
    public Transfer sendTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
        long newTransferId = jdbcTemplate.queryForObject(sql, Long.class, transfer.getTransferTypeId(),
                transfer.getTransferStatusId(), transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
        transfer.setTransferId(newTransferId);
        return transfer;
    }


    private Account mapRowToAccount(ResultSet rs, int rowNum) throws SQLException {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }

    private Transfer mapRowToTransfer(ResultSet rs, int rowNum) throws SQLException {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getLong("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }
}
