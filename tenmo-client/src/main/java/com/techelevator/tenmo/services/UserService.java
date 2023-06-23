package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
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

    // Constructor to initialize the UserService with a base URL
    public UserService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    // Sets the authenticated user for the UserService
    public void setAuthenticatedUser(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    // Retrieves all users from the server
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try {
            // Send a GET request to retrieve all users
            ResponseEntity<User[]> response = restTemplate.exchange(baseUrl + "/users", HttpMethod.GET, makeAuthEntity(), User[].class);
            if (response.hasBody()) {
                users = Arrays.asList(response.getBody());
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return users;
    }

    // Checks if a user ID is valid and exists
    public boolean isUserValid(int userId, User currentUser) {
        if (userId == currentUser.getId()) {
            System.out.println("You can't send money to yourself!\n");
            return false;
        }

        if (userId == 0) {
            System.out.println("Returning to the main menu...");
            return false;
        }

        try {
            // Send a GET request to check if the user with the given ID exists
            ResponseEntity<User> response = restTemplate.exchange(baseUrl + "/users/search_user/" + userId, HttpMethod.GET, makeAuthEntity(), User.class);
            if (response.hasBody()) {
                return true;
            } else {
                System.out.println("User not found");
            }
        } catch (RestClientResponseException e) {
            int statusCode = e.getRawStatusCode();
            if (statusCode == 404) {
                System.out.println("User not found");
            } else {
                System.out.println("Error: The server returned a " + statusCode + " status code.");
            }
        } catch (ResourceAccessException e) {
            System.out.println("Error: Unable to access the server. Please check your network connection.");
        }

        return false;
    }

    // Retrieves usernames from transfers for the given account
    public List<String> getUsersFromTransfers(List<Transfer> transfers, Account currentAccount) {
        List<String> usersFromTransfers = new ArrayList<>();

        for (Transfer transfer : transfers) {
            String username;
            if (transfer.getAccountFrom() == currentAccount.getAccountID()) {
                username = getUsernameFromAccount(transfer.getAccountTo());
            } else {
                username = getUsernameFromAccount(transfer.getAccountFrom());
            }

            usersFromTransfers.add(username);
        }

        return usersFromTransfers;
    }

    // Retrieves the username associated with the given account
    public String getUsernameFromAccount(int account) {
        String username = "";

        try {
            // Send a GET request to retrieve the username associated with the account
            ResponseEntity<String> response = restTemplate.exchange(baseUrl + "/users/user_from_account/" + account, HttpMethod.GET, makeAuthEntity(), String.class);
            if (response.hasBody()) {
                username = response.getBody();
            }
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return username;
    }

    // Checks if an amount is valid
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

    // Creates an HttpEntity with authentication headers only
    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity<>(headers);
    }
}

