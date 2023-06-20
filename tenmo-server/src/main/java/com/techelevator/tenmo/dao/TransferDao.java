package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    Transfer createTransfer(Transfer transfer);

    List<Transfer> getAllTransfersOfUserId(int id);

    Transfer getTransfer(int id);

    List<Transfer> getPendingTransfers(int id);

    boolean updateTransfer(Transfer transfer);
}
