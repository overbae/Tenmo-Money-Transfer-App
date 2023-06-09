package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferStatusDto;
public interface TransferStatusDao {
    TransferStatusDto getTransferStatusById(int statusId);

    TransferStatusDto getTransferStatusByDesc(String desc);
}
