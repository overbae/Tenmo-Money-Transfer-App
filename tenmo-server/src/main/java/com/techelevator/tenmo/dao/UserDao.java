package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.util.List;

public interface UserDao {

    List<User> findAll();

//    boolean createAdmin(String username, String password);
//    User getUserByAccount(String accountId);

    String getUserByAccount(int accountId);

    User getUserById(int id);

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password);
}
