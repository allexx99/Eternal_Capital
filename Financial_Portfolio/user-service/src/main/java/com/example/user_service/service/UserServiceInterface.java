package com.example.user_service.service;

import com.example.user_service.dto.UserDTO;
import com.example.user_service.model.User;

import java.util.List;

public interface UserServiceInterface {
    User addUser(UserDTO userDTO, String jwt);
    List<User> getUsers();
    void updateUser(long id, UserDTO userDTO);
    void deleteUser(long id, String jwt);
    User getUsersById(long id);
    UserDTO findUserByUsername(String username);
}
