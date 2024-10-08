package com.cydeo.service;

import com.cydeo.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> listAllUsers();//list all

    UserDTO findByUserName(String username); //to update--> we need this one to update

    void save(UserDTO user); // save

    void deleteByUserName(String username); //delete

    UserDTO update(UserDTO user);
    void delete(String username);
    List<UserDTO> listAllByRole(String role);


}

//we are in the service layer, meaning is called this method from controller.
// Controller will call this method whatever controller we say UI
//what is coming from the UI? DTO
//Everything is separated
