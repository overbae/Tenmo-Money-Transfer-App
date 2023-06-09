package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.UserDto;

import java.util.List;

public interface UserDao {

    List<UserDto> findAll();

    UserDto getUserById(int id);

    UserDto findByUsername(String username);

    int findIdByUsername(String username);

    boolean createAdmin(String username, String password);
    boolean create(String username, String password);
}
