package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/dashboard/transfer")
@PreAuthorize("isAuthenticated()")
public class TransferController {
    private final TransferDao transferDao;
    private final UserDao userDao;

    public TransferController(JdbcTransferDao transferDao, JdbcUserDao userDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Transfer createTransfer(@RequestBody @Valid Transfer transfer) {
        transfer = transferDao.createTransfer(transfer);
        return transfer;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/transfers_history")
    public List<Transfer> getTransfers(Principal principal) {
        int id = userDao.findIdByUsername(principal.getName());
        return transferDao.getAllTransfersOfUserId(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/pending_transfers")
    public List<Transfer> getPendingTransfers(Principal principal) {
        int id = userDao.findIdByUsername(principal.getName());
        return transferDao.getPendingTransfers(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}")
    public Transfer getTransferById(@PathVariable int id) {
        return transferDao.getTransfer(id);
    }

    @PutMapping("/transfer_approved")
    public ResponseEntity<Boolean> updateTransfer(@RequestBody @Valid Transfer transfer) {
        if (transferDao.updateTransfer(transfer)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}