package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.model.TransferDto;

import java.math.BigDecimal;
import java.util.List;
public interface TransferDao {
    public List <TransferDto> getAllTransfersByUserId(int userId);

    List<TransferDto> seeAllTransfers();

    public Object getTransferById(int transferId);
    public String sendTransfer(int userFromId, int userToId, BigDecimal amount);
    public String requestTransfer(int userFrom, int userTo, BigDecimal amount);
    public List<TransferDto> getPendingRequests(int userId);
    public String updateTransferRequest(TransferDto transfer, int statusId);

}
//comment