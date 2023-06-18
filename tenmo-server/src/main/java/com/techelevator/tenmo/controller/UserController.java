package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("user")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserDao userDao;

    public UserController(UserDao userDao){
        this.userDao = userDao;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public List<User> listAllUsers() {
        return userDao.findAll();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserByUserId(@PathVariable int userId) {
        User user = userDao.getUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(params = "username")
    public ResponseEntity<User> findByUsername(@RequestParam("username") String username) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/userId/{username}")
    public int findIdByUsername(@PathVariable String username) {
        return userDao.findIdByUsername(username);
    }
}
