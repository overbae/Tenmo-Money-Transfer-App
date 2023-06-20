package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserService {
    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser authenticatedUser;

    public UserService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setAuthenticatedUser(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try {
            ResponseEntity<User[]> response = restTemplate.exchange(baseUrl + "/users", HttpMethod.GET, makeAuthEntity(), User[].class);
            if (response.hasBody()) {
                users = Arrays.asList(response.getBody());
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return users;
    }

    public boolean isUserValid(int userId, User currentUser) {
        if (userId == currentUser.getId()) {
            System.out.println("You can't send money to yourself!\n");
            return false;
        }

        try {
            ResponseEntity<User> response = restTemplate.exchange(baseUrl + "/users/search_user/" + userId, HttpMethod.GET, makeAuthEntity(), User.class);
            if (response.hasBody()) {
                return true;
            } else {
                System.out.println("User not found");
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return false;
    }

    public List<String> getUsersFromTransfers(List<Transfer> transfers) {
        List<String> usersFromTransfers = new ArrayList<>();

        for (Transfer transfer : transfers) {
            String username = getUsernameFromAccount(transfer.getAccountTo());
            usersFromTransfers.add(username);
        }

        return usersFromTransfers;
    }

    public String getUsernameFromAccount(int account) {
        String username = "";

        try {
            ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/users/user_from_account/" + account, HttpMethod.GET, makeAuthEntity(), String.class);
            if (response.hasBody()) {
                username = response.getBody();
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return username;
    }

    public boolean isAmountValid(BigDecimal amount) {
        if (amount.equals(BigDecimal.ZERO)) {
            System.out.println("Transfer has been cancelled.");
            return false;
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("Amount can't be negative.");
            return false;
        }

        return true;
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity<>(headers);
    }
}

