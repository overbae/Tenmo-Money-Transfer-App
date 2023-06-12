package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("user")
public class UserController {
    UserDao userDao;

    public UserController(UserDao userDao){
        this.userDao = userDao;
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<UserDto> listAllUsers() {
        return userDao.findAll();
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping (path = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<UserDto> getUserByUserId(@PathVariable int userId) {
        UserDto user = userDao.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(user);
        }
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping (path = "/{username}", method = RequestMethod.GET)
    public ResponseEntity<Object> findByUsername(@RequestParam("username") String username) {
        UserDto user = userDao.findByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(user);
        }
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping (path = "user/userId/{username}", method = RequestMethod.GET)
    public int findIdByUsername(String username) {
        return userDao.findIdByUsername(username);
    }
}
