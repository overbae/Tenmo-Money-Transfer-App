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

    public AccountService(String url) {
        this.baseUrl = url;
        this.restTemplate = new RestTemplate();
    }

    public AuthenticatedUser getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthenticatedUser(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public void updateAccount(Account account) {
        try {
            HttpEntity<Account> entity = makeAccountEntity(account);
            restTemplate.put(baseUrl + "/dashboard/account", entity);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }

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

    public Account getUserAccount() {
        List<Account> accounts = getUserAccounts();
        if (accounts != null && accounts.size() == 1) {
            return accounts.get(0);
        }
        return null;
    }

    public boolean hasMultipleAccounts(List<Account> accounts) {
        return accounts != null && accounts.size() > 1;
    }

    public boolean isAmountInAccountRange(BigDecimal amountSend, BigDecimal accountBalance) {
        return amountSend.compareTo(accountBalance) > 0;
    }

    private HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = createHeadersWithToken();
        return new HttpEntity<>(account, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = createHeadersWithToken();
        return new HttpEntity<>(headers);
    }

    private HttpHeaders createHeadersWithToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}