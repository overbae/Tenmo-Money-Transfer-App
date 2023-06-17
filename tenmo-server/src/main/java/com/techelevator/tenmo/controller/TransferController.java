package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transfer")
public class TransferController {
    private final TransferDao transferDao;

    public TransferController(TransferDao transferDao) {
        this.transferDao = transferDao;
    }

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.CREATED)
    public Transfer sendTransfer(@RequestBody Transfer transfer) {
        int accountFrom = transferDao.getAccountIdByUserId(transfer.getAccountFrom());
        int accountTo = transferDao.getAccountIdByUserId(transfer.getAccountTo());
        transfer.setAccountFrom(accountFrom);
        transfer.setAccountTo(accountTo);
        return transferDao.sendTransfer(transfer);
    }

    @GetMapping("/{transferId}")
    public Transfer getTransferById(@PathVariable int transferId) {
        return transferDao.getTransferById(transferId);
    }

    @GetMapping("/user/{userId}")
    public List<Transfer> getTransfersByUserId(@PathVariable int userId) {
        return transferDao.getTransfersByUserId(userId);
    }

    @GetMapping("/all")
    public List<Transfer> seeAllTransfers() {
        return transferDao.seeAllTransfers();
    }

    @PostMapping("/request")
    @ResponseStatus(HttpStatus.CREATED)
    public String requestTransfer(@RequestParam int userFrom, @RequestParam int userTo, @RequestParam BigDecimal amount) {
        return transferDao.requestTransfer(userFrom, userTo, amount);
    }

    @GetMapping("/pending/{userId}")
    public List<Transfer> getPendingRequests(@PathVariable int userId) {
        return transferDao.getPendingRequests(userId);
    }

    @PutMapping("/{transferId}")
    public String updateTransferRequest(@RequestBody Transfer transfer, @PathVariable int transferId) {
        return transferDao.updateTransferRequest(transfer, transferId);
    }
}
