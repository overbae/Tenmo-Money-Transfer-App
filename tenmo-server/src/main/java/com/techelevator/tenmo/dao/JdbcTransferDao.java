package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer createTransfer(Transfer newTransfer) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "RETURNING transfer_id;";
        Integer transferId = jdbcTemplate.queryForObject(sql, Integer.class, newTransfer.getTransferTypeId(), newTransfer.getTransferStatusId(), newTransfer.getAccountFrom(),
                newTransfer.getAccountTo(), newTransfer.getAmount());
        if (transferId != null) {
            newTransfer.setTransferId(transferId);
        }
        return newTransfer;
    }

//    @Override
//    public Transfer sendTransfer(Transfer transfer) {
//        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
//                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id";
//        Long transferId = jdbcTemplate.queryForObject(sql, Long.class,
//                transfer.getTransferTypeId(),
//                transfer.getTransferStatusId(),
//                transfer.getAccountFrom(),
//                transfer.getAccountTo(),
//                transfer.getAmount());
//
//        // Set the generated transfer ID in the transfer object
//        transfer.setTransferId(transferId);
//
//        // Update the account balances
//        updateAccountBalances(transfer);
//
//        return transfer;
//    }

    @Override
    public List<Transfer> getAllTransfersOfUserId(int id) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
                "JOIN account ON account_from = account_id " +
                "WHERE account.user_id = ? OR transfer.account_to = (SELECT account_id FROM account WHERE user_id = ?);";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, id);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public Transfer getTransfer(int id) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
            transfer = mapRowToTransfer(results);
        }
        return transfer;
    }

    @Override
    public List<Transfer> getPendingTransfers(int id) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
                "JOIN account ON account_from = account_id " +
                "JOIN transfer_status USING (transfer_status_id) " +
                "WHERE account.user_id = ? AND transfer_status_desc ILIKE 'Pending';";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public boolean updateTransfer(Transfer transfer) {
        String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?;";
        return jdbcTemplate.update(sql, transfer.getTransferStatusId(), transfer.getTransferId()) == 1;
    }

//    private void updateAccountBalances(Transfer transfer) {
//        BigDecimal amount = transfer.getAmount();
//        int accountFrom = transfer.getAccountFrom();
//        int accountTo = transfer.getAccountTo();
//
//        String subtractSql = "UPDATE account SET balance = balance - ? WHERE account_id = ?";
//        String addSql = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
//
//        jdbcTemplate.update(subtractSql, amount, accountFrom);
//        jdbcTemplate.update(addSql, amount, accountTo);
//    }

//    @Override
//    public List<Transfer> getTransfersByUserId(int userId) {
//        String sql = "SELECT * FROM transfer WHERE account_from = ? OR account_to = ?";
//        return jdbcTemplate.query(sql, this::mapRowToTransfer, userId, userId);
//    }

//    @Override
//    public Transfer getTransferById(int transferId) {
//        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
//        return jdbcTemplate.queryForObject(sql, this::mapRowToTransfer, transferId);
//    }

//    @Override
//    public List<Transfer> seeAllTransfers() {
//        String sql = "SELECT * FROM transfer";
//        return jdbcTemplate.query(sql, this::mapRowToTransfer);
//    }

//    @Override
//    public String sendTransfer(int userFromId, int userToId, BigDecimal amount) {
//        // Create a new Transfer object with the provided details
//        Transfer transfer = new Transfer();
//        transfer.setTransferTypeId(2); // Assuming 2 represents a "Send" transfer type
//        transfer.setTransferStatusId(2); // Assuming 2 represents an "Approved" transfer status
//        transfer.setAccountFrom(userFromId);
//        transfer.setAccountTo(userToId);
//        transfer.setAmount(amount);
//
//        // Call the existing sendTransfer() method to insert the transfer into the database
//        Transfer insertedTransfer = sendTransfer(transfer);
//
//        // Check if the transfer was inserted successfully and return the appropriate message
//        if (insertedTransfer != null && insertedTransfer.getTransferId() != null) {
//            return "Transfer sent successfully.";
//        } else {
//            return "Failed to send transfer.";
//        }
//    }

//    @Override
//    public String requestTransfer(int userFromId, int userToId, BigDecimal amount) {
//        // Create a new Transfer object with the provided details
//        Transfer transfer = new Transfer();
//        transfer.setTransferTypeId(1); // Assuming 1 represents a "Request" transfer type
//        transfer.setTransferStatusId(1); // Assuming 1 represents a "Pending" transfer status
//        transfer.setAccountFrom(userFromId);
//        transfer.setAccountTo(userToId);
//        transfer.setAmount(amount);
//
//        // Call the existing sendTransfer() method to insert the transfer into the database
//        Transfer insertedTransfer = sendTransfer(transfer);
//
//        // Check if the transfer was inserted successfully and return the appropriate message
//        if (insertedTransfer != null && insertedTransfer.getTransferId() != null) {
//            return "Transfer request sent successfully.";
//        } else {
//            return "Failed to send transfer request.";
//        }
//    }


//    @Override
//    public List<Transfer> getPendingRequests(int userId) {
//        // Implement this method if needed
//        return null;
//    }

//    @Override
//    public String updateTransferRequest(Transfer transfer, int statusId) {
//        // Implement this method if needed
//        return null;
//    }

//    @Override
//    public int getAccountIdByUserId(int userId) {
//        String sql = "SELECT account_id FROM account WHERE user_id = ?";
//        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
//    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setAccountFrom(rowSet.getInt("account_from"));
        transfer.setAccountTo(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        return transfer;
    }
}
