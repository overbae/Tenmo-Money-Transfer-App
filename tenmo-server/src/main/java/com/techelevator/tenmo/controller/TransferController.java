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
// comment for push
@RestController
@RequestMapping("/dashboard/transfer")
@PreAuthorize("isAuthenticated()")
public class TransferController {
    private final TransferDao transferDao;
    private final UserDao userDao;

    public TransferController(TransferDao transferDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    // Handles the POST request to create a new transfer
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transfer createTransfer(@RequestBody @Valid Transfer transfer) {
        // Create the transfer using the provided transfer object
        transfer = transferDao.createTransfer(transfer);
        return transfer;
    }

    // Handles the GET request to retrieve all transfers history for a user
    @GetMapping(path = "/transfers_history")
    @ResponseStatus(HttpStatus.OK)
    public List<Transfer> getTransfers(Principal principal) {
        // Retrieve the user's ID based on the principal's name (username)
        int id = userDao.findIdByUsername(principal.getName());
        // Return all transfers associated with the user's ID
        return transferDao.getAllTransfersOfUserId(id);
    }

    // Handles the GET request to retrieve all pending transfers for a user
    @GetMapping(path = "/pending_transfers")
    @ResponseStatus(HttpStatus.OK)
    public List<Transfer> getPendingTransfers(Principal principal) {
        // Retrieve the user's ID based on the principal's name (username)
        int id = userDao.findIdByUsername(principal.getName());
        // Return all pending transfers associated with the user's ID
        return transferDao.getPendingTransfers(id);
    }

    // Handles the GET request to retrieve a transfer by its ID
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Transfer getTransferById(@PathVariable int id) {
        // Retrieve the transfer with the provided ID
        return transferDao.getTransfer(id);
    }

    // Handles the PUT request to update the status of a transfer to "approved"
    @PutMapping("/transfer_approved")
    public ResponseEntity<Boolean> updateTransfer(@RequestBody @Valid Transfer transfer) {
        // Update the transfer using the provided transfer object
        if (transferDao.updateTransfer(transfer)) {
            return ResponseEntity.ok().build();
        }
        // If the transfer update fails, return an appropriate response with "NOT_FOUND" status
        return ResponseEntity.notFound().build();
    }
}
