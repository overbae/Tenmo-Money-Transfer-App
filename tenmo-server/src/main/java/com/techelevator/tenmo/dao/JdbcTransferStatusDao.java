package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatus;

public class JdbcTransferStatusDao implements TransferStatusDao {
    @Override
    public TransferStatus getTransferStatusById(int statusId) {
        return null;
    }

    @Override
    public TransferStatus getTransferStatusByDesc(String desc) {
        return null;
    }
}
