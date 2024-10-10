package com.soft.electronic.store.services;

import com.soft.electronic.store.dtos.PageableResponse;
import com.soft.electronic.store.dtos.UserDTO;
import com.soft.electronic.store.entities.User;

import java.util.List;

public interface UserService {

    //create
    UserDTO createUser(UserDTO userDTO);

    //update
    UserDTO updateUser(UserDTO userDTO, String userId);

    //delete
    void deleteUser(String userId);

    //getallUser
    PageableResponse<UserDTO> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    //getSingleUserByID
    UserDTO getUserById(String userId);

    //getSingleUserByEmail
    UserDTO getUserByEmail(String userId);

    //searchUser
    List<UserDTO> searchUser(String keywords);
}
