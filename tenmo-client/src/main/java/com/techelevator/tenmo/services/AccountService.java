package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


public class AccountService {
    private final String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser authenticatedUser;

    //Constructs an AccountService object with the specified base URL.

    public AccountService(String url) {
        this.baseUrl = url;
        this.restTemplate = new RestTemplate();
    }

    //Returns the AuthenticatedUser object representing the currently authenticated user.
    public AuthenticatedUser getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthenticatedUser(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }


//     * Updates the account information on the server by sending an HTTP PUT request.

    public void updateAccount(Account account) {
        try {
            HttpEntity<Account> entity = makeAccountEntity(account);
            restTemplate.put(baseUrl + "/dashboard/account", entity);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }


//     * Retrieves the account with the specified ID from the server by sending an HTTP GET request.

    public Account getAccount(int id) {
        try {
            ResponseEntity<Account> response = restTemplate.exchange(
                    baseUrl + "/dashboard/account/" + id,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Account.class
            );
            return response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            return null;
        }
    }


//     * Retrieves the account with the specified ID from the server by sending an HTTP GET request.

    public Account getAccountById(int id) {
        try {
            ResponseEntity<Account> response = restTemplate.exchange(
                    baseUrl + "/dashboard/get_account/" + id,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Account.class
            );
            return response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            return null;
        }
    }


//     * Retrieves the list of user accounts from the server by sending an HTTP GET request.

    public List<Account> getUserAccounts() {
        try {
            ResponseEntity<Account[]> response = restTemplate.exchange(
                    baseUrl + "/dashboard/accounts",
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Account[].class
            );
            return Arrays.asList(response.getBody());
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            return null;
        }
    }


//     * Retrieves the list of transfers from the server by sending an HTTP GET request.

    public List<Transfer> getTransfers() {
        try {
            ResponseEntity<Transfer[]> responseEntity = restTemplate.exchange(
                    baseUrl + "/dashboard/transfer/transfers_history",
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer[].class
            );
            return Arrays.asList(responseEntity.getBody());
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            return null;
        }
    }


//     * Retrieves the list of pending transfers from the server by sending an HTTP GET request.

    public List<Transfer> getPendingTransfers() {
        try {
            ResponseEntity<Transfer[]> responseEntity = restTemplate.exchange(
                    baseUrl + "/dashboard/transfer/pending_transfers",
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer[].class
            );
            return Arrays.asList(responseEntity.getBody());
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            return null;
        }
    }


//     * Retrieves the user account associated with the currently authenticated user.

    public Account getUserAccount() {
        List<Account> accounts = getUserAccounts();
        if (accounts != null && accounts.size() == 1) {
            return accounts.get(0);
        }
        return null;
    }


//     * Checks if the given amount is within the range of the account balance.

    public boolean isAmountInAccountRange(BigDecimal amountSend, BigDecimal accountBalance) {
        return amountSend.compareTo(accountBalance) > 0;
    }


//    Creates an HttpEntity object with the given Account and authentication headers.

    private HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = createHeadersWithToken();
        return new HttpEntity<>(account, headers);
    }

// Creates an HttpEntity object with the authentication headers.
    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = createHeadersWithToken();
        return new HttpEntity<>(headers);
    }

    //Creates HttpHeaders object with the authentication token and content type.
    private HttpHeaders createHeadersWithToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}