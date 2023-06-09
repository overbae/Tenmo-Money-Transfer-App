package com.techelevator.tenmo.services;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class UserService {
    private final String BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();
    public AuthenticatedUser user;

    public UserService(String url){
        BASE_URL = url;
    }

    // Set the authenticated user
    public void setUser(AuthenticatedUser user){
        this.user = user;
    }

    // Create the HTTP entity with authentication headers
    public HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(headers);
    }

    // Find a user by their ID
    public User findUserById(int id){
        User user = null;
        try{
            // Send a GET request to the API to retrieve the user by ID
            user = restTemplate.exchange(BASE_URL + "/" + id, HttpMethod.GET, makeAuthEntity(), User.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e){
            System.out.println("User not found.");
        }
        return user;
    }

    // Retrieve all users
    public User[] findAllUsers(AuthenticatedUser user){
        User[] allUsers = null;
        try{
            // Set the authenticated user
            setUser(user);
            // Send a GET request to the API to retrieve all users
            allUsers = restTemplate.exchange(BASE_URL, HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e){
            System.out.println("Users not found.");
        }
        return allUsers;
    }

    // List all users (excluding the authenticated user)
    public void listAllUsers(AuthenticatedUser user) {
        User[] allUsers = findAllUsers(user);

        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println("LIST OF AVAILABLE USERS");
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println("Users");
        System.out.println("ID\t\t\t\tName");
        System.out.println("-------------------------------------------------------------------------------------------");

        for (User u : allUsers) {
            if (u.getId() != user.getUser().getId()) {
                // Display the ID and username of each user
                System.out.println(u.getId() + "\t\t\t" + u.getUsername());
            }
        }

        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println();
    }
}

//