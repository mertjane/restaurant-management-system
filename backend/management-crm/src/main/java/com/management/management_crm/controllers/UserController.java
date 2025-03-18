package com.management.management_crm.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.management_crm.models.UserEntity;
import com.management.management_crm.repository.UserRepository;


@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;


    // @Get Method for fetching all users
    @GetMapping("/users")
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
}
