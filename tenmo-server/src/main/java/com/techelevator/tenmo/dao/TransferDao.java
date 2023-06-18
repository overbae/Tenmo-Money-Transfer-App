package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    Transfer sendTransfer(Transfer transfer);

    List<Transfer> getTransfersByUserId(int userId);

    Transfer getTransferById(int transferId);

    List<Transfer> seeAllTransfers();

    String sendTransfer(int userFromId, int userToId, BigDecimal amount);

    String requestTransfer(int userFrom, int userTo, BigDecimal amount);

    List<Transfer> getPendingRequests(int userId);

    String updateTransferRequest(Transfer transfer, int statusId);

    int getAccountIdByUserId(int userId);
}
