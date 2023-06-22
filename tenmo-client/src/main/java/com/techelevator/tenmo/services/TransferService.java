package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransferService {
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser authenticatedUser;

    // Constructor to initialize the TransferService with a base URL
    public TransferService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    // Creates a new transfer by making a POST request to the server
    public Transfer createTransfer(Transfer transfer) {
        HttpEntity<Transfer> entity = createTransferEntity(transfer);
        Transfer returnedTransfer = null;

        try {
            // Send the POST request and retrieve the created transfer
            returnedTransfer = restTemplate.postForObject(baseUrl + "dashboard/transfer", entity, Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return returnedTransfer;
    }

    // Updates an existing transfer by making a PUT request to the server
    public boolean updateTransfer(Transfer transfer) {
        HttpEntity<Transfer> entity = createTransferEntity(transfer);
        boolean wasUpdated = false;

        try {
            // Send the PUT request to update the transfer
            restTemplate.put(baseUrl + "/dashboard/transfer/transfer_approved", entity);
            wasUpdated = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return wasUpdated;
    }

    // Checks if a transfer ID is valid
    public boolean isTransferValid(int transferId) {
        if (transferId == 0) {
            System.out.println("Returning to the main menu...");
            return false;
        }
        Transfer transfer = getTransferById(transferId);

        if (transfer == null) {
            System.out.println("You don't have a transfer with the ID " + transferId + ".");
            System.out.println("Returning to the main menu...");
            return false;
        }
        return true;
    }

    // Checks if a transfer choice is valid
    public boolean isTransferChoiceValid(int transferChoice) {
        if (transferChoice == 0) {
            System.out.println("Returning to the main menu...");
            return false;
        }

        if (transferChoice != 1 && transferChoice != 2) {
            System.out.println("Not a valid selection.");
            System.out.println("Returning to the main menu...");
            return false;
        }
        return true;
    }

    // Retrieves a transfer by its ID
    public Transfer getTransferById(int id) {
        Transfer transfer = null;

        try {
            // Send a GET request to retrieve the transfer by ID
            ResponseEntity<Transfer> response = restTemplate.exchange(baseUrl + "/dashboard/transfer/" + id, HttpMethod.GET, makeAuthEntity(), Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    // Sets the authenticated user for the TransferService
    public void setAuthenticatedUser(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    // Sets the details of a transfer
    public Transfer setTransferDetails(Transfer transfer, int type, int status, int accountFrom, int accountTo, BigDecimal amount) {
        transfer.setTransferTypeId(type);
        transfer.setTransferStatusId(status);
        transfer.setAccountFrom(accountFrom);
        transfer.setAccountTo(accountTo);
        transfer.setAmount(amount);
        return transfer;
    }

    // Creates an HttpEntity with the Transfer object and authentication headers
    private HttpEntity<Transfer> createTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity<>(transfer, headers);
    }

    // Creates an HttpEntity with authentication headers only
    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity<>(headers);
    }
}
