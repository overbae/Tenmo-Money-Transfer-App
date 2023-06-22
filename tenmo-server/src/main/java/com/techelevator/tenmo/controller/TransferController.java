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

    // Handles the POST request to create a new transfer
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Transfer createTransfer(@RequestBody @Valid Transfer transfer) {
        // Create the transfer using the provided transfer object
        transfer = transferDao.createTransfer(transfer);
        return transfer;
    }

    // Handles the GET request to retrieve all transfers history for a user
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/transfers_history")
    public List<Transfer> getTransfers(Principal principal) {
        // Retrieve the user's ID based on the principal's name (username)
        int id = userDao.findIdByUsername(principal.getName());
        // Return all transfers associated with the user's ID
        return transferDao.getAllTransfersOfUserId(id);
    }

    // Handles the GET request to retrieve all pending transfers for a user
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/pending_transfers")
    public List<Transfer> getPendingTransfers(Principal principal) {
        // Retrieve the user's ID based on the principal's name (username)
        int id = userDao.findIdByUsername(principal.getName());
        // Return all pending transfers associated with the user's ID
        return transferDao.getPendingTransfers(id);
    }

    // Handles the GET request to retrieve a transfer by its ID
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}")
    public Transfer getTransferById(@PathVariable int id) {
        // Retrieve the transfer with the provided ID
        return transferDao.getTransfer(id);
    }

    // Handles the PUT request to update the status of a transfer to "approved"
    @PutMapping("/transfer_approved")
    public ResponseEntity<Boolean> updateTransfer(@RequestBody @Valid Transfer transfer) {
        // Update the transfer using the provided transfer object
        if (transferDao.updateTransfer(transfer)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        // If the transfer update fails, return an appropriate response with "NOT_FOUND" status
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
