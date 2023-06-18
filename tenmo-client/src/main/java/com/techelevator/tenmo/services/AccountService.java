package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser user;

    public AccountService(String baseUrl) {
//        System.out.println(baseUrl);
        this.baseUrl = baseUrl;
    }

    public void setUser(AuthenticatedUser user) {
        this.user = user;
    }

    public BigDecimal getBalance(AuthenticatedUser user) {
        BigDecimal balance = new BigDecimal(0);
        try {
            setUser(user);
            // Retrieve the account balance for the authenticated user
            String url = baseUrl + "/balance";  // Remove the redundant "/accounts" part
            ResponseEntity<BigDecimal> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
            balance = response.getBody();

//            System.out.println("DEBUG: GET " + url);
//            System.out.println("DEBUG: Response status: " + response.getStatusCodeValue());
//            System.out.println("DEBUG: Response body: " + balance);
        } catch (RestClientException e) {
            System.out.println("Your balance could not be retrieved.");
            System.out.println("DEBUG: Exception message: " + e.getMessage());
        }
        return balance;
    }



//    public Account findAccountByUserId(int userId) {
//        Account account = null;
//        try {
//            account = restTemplate.exchange(baseUrl + "/user/" + userId, HttpMethod.GET, makeAuthEntity(), Account.class).getBody();
//        } catch (RestClientException e) {
//            System.out.println("This account could not be found.");
//        }
//        return account;
//    }

    public Account findAccountByUserId(int userId) {
        Account account = null;
        try {
            String url = baseUrl + "/user/" + userId;
            ResponseEntity<Account> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(), Account.class);

//                System.out.println("DEBUG: GET " + url);
//                System.out.println("DEBUG: Response status: " + response.getStatusCodeValue());
//                System.out.println("DEBUG: Response body: " + response.getBody());

            account = response.getBody();
        } catch (RestClientException e) {
            System.out.println("This account could not be found.");
            System.out.println("DEBUG: Exception message: " + e.getMessage());
        }
        return account;
    }

    public Transfer sendBucks(AuthenticatedUser user, int destinationUserId, BigDecimal amount) {
        Transfer transfer = new Transfer();
        transfer.setUserFrom(user.getUser().getId());
        transfer.setUserTo(destinationUserId);
        transfer.setAmount(amount);
        transfer.setAccountFrom(findAccountByUserId(user.getUser().getId()).getAccountId());
        Account recipientAccount = findAccountByUserId(destinationUserId);
        transfer.setAccountTo(recipientAccount.getAccountId());

        try {
            String url = baseUrl + "/transfer/send";
            ResponseEntity<Transfer> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    makeAccountEntity(transfer),
                    Transfer.class
            );

            System.out.println("DEBUG: POST " + url);
            System.out.println("DEBUG: Request body: " + transfer);
            System.out.println("DEBUG: Response status: " + response.getStatusCodeValue());
            System.out.println("DEBUG: Response body: " + response.getBody());

            return response.getBody();
        } catch (RestClientException e) {
            System.out.println("Failed to send money. Please try again.");
            System.out.println("DEBUG: Exception message: " + e.getMessage());
        }
        return null;
    }



//    public Transfer sendBucks(int destinationUserId, BigDecimal amount) {
//        Transfer transfer = new Transfer(user.getUser().getId(), destinationUserId, amount);
//        try {
//            ResponseEntity<Transfer> response = restTemplate.exchange(
//                    baseUrl + "/accounts/transfer/send",
//                    HttpMethod.POST,
//                    makeAccountEntity(transfer),
//                    Transfer.class
//            );
//            return response.getBody();
//        } catch (RestClientException e) {
//            System.out.println("Failed to send money. Please try again.");
//        }
//        return null;
//    }



    public Transfer requestBucks(int sourceUserId, BigDecimal amount) {
        Transfer transfer = new Transfer(sourceUserId, user.getUser().getId(), amount);
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(
                    baseUrl + "accounts/transfer/request",
                    HttpMethod.POST,
                    makeAccountEntity(transfer),
                    Transfer.class
            );
            return response.getBody();
        } catch (RestClientException e) {
            System.out.println("Failed to request money. Please try again.");
        }
        return null;
    }

    public Transfer[] getTransferHistory() {
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(
                    baseUrl + "accounts/transfer",
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer[].class
            );
            return response.getBody();
        } catch (RestClientException e) {
            System.out.println("Failed to retrieve transfer history.");
        }
        return null;
    }

    public HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(headers);
    }

    public HttpEntity<Transfer> makeAccountEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(transfer, headers);
    }
}
