package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
@PreAuthorize("isAuthenticated()")
public class UserController {

    private UserDao userDao;

    public UserController(JdbcUserDao userDao) {
        this.userDao = userDao;
    }

    // Handles the GET request to find the ID of the currently logged-in user
    @GetMapping(path = "/id")
    public int findIdByUserName(Principal principal) {
        // Check if the principal's name is not null
        if (principal.getName() != null) {
            // Retrieve and return the ID of the user based on the principal's name (username)
            return userDao.findIdByUsername(principal.getName());
        }
        // If the principal's name is null, throw a "NOT_FOUND" exception
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
    }

    // Handles the GET request to retrieve a user by their ID
    @GetMapping(path = "/search_user/{id}")
    public User getUserById(@PathVariable int id) {
        // Retrieve the user with the provided ID
        User user = userDao.getUserById(id);

        // Check if the user is null
        if (user == null) {
            // If the user is null, throw a "NOT_FOUND" exception
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Auction Not Found");
        } else {
            // If the user exists, return the user object
            return user;
        }
    }

    // Handles the GET request to retrieve the username associated with an account ID
    @GetMapping(path = "/user_from_account/{id}")
    public String getUsernameByAccountId(@PathVariable int id) {
        // Retrieve the username associated with the provided account ID
        return userDao.getUserByAccount(id);
    }

    // Handles the GET request to retrieve all users
    @GetMapping
    public List<User> getUsers() {
        // Return a list of all users
        return userDao.findAll();
    }
}
