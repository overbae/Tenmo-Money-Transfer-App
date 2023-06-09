package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.model.TransferTypeDto;
public interface TransferTypeDao {
    TransferTypeDto getTransferTypeById(int typeId);

    TransferTypeDto getTransferTypeByDesc(String desc);
}
