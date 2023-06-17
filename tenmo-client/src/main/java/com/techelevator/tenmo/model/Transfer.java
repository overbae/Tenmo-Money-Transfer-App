package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class Transfer {
    private int transferId;
    private int transferStatusId;
    private int transferTypeId;
    private int accountTo;
    private int accountFrom;
    private BigDecimal amount;

    private int userTo;
    private int userFrom;
    private String userToUsername;
    private String userFromUsername;
    private String transferStatus;
    private String transferType;

    public Transfer() {
        // Default constructor
    }

    public Transfer(int id, int destinationUserId, BigDecimal amount) {
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getUserTo() {
        return userTo;
    }

    public void setUserTo(int userTo) {
        this.userTo = userTo;
    }

    public int getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(int userFrom) {
        this.userFrom = userFrom;
    }

    public String getUserToUsername() {
        return userToUsername;
    }

    public void setUserToUsername(String userToUsername) {
        this.userToUsername = userToUsername;
    }

    public String getUserFromUsername() {
        return userFromUsername;
    }

    public void setUserFromUsername(String userFromUsername) {
        this.userFromUsername = userFromUsername;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String displayTransferType(Integer transferTypeId) {
        if (transferTypeId == 1) {
            return "Request Money";
        } else if (transferTypeId == 2) {
            return "Send Money";
        }
        return transferTypeId.toString();
    }

    public String displayTransferStatus(Integer transferStatusId) {
        if (transferStatusId == 1) {
            return "Pending";
        } else if (transferStatusId == 2) {
            return "Approved";
        } else if (transferStatusId == 3) {
            return "Rejected";
        } else {
            return transferStatusId.toString();
        }
    }

    public String displayAsCurrency(BigDecimal bigDecimal) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(bigDecimal);
    }

    public String transferDetailsPrintOut() {
        StringBuilder formattedMenu = new StringBuilder();

        formattedMenu.append("-------------------------------------------------------------------------------------------\n");
        formattedMenu.append("Transfer Details:\n");
        formattedMenu.append("-------------------------------------------------------------------------------------------\n");
        formattedMenu.append("Transfer ID:    ").append(transferId).append("\n");

        formattedMenu.append("From User ID:   ").append(userFrom).append("\n");
        formattedMenu.append("To User ID:     ").append(userTo).append("\n");
        formattedMenu.append("From User:      ").append(userFromUsername).append("\n");
        formattedMenu.append("To User:        ").append(userToUsername).append("\n");
        formattedMenu.append("Type:           ").append(displayTransferType(transferTypeId)).append("\n");
        formattedMenu.append("Status:         ").append(displayTransferStatus(transferStatusId)).append("\n");
        formattedMenu.append("Amount:         ").append(displayAsCurrency(amount)).append("\n");
        formattedMenu.append("-------------------------------------------------------------------------------------------");

        return formattedMenu.toString();
    }
}
