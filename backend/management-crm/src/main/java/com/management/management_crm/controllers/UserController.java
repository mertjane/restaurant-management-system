package com.management.management_crm.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.management_crm.dto.UserDTO;
import com.management.management_crm.models.UserEntity;
import com.management.management_crm.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // @Get Method for fetching all users
    @GetMapping("/users")
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // @Get Method for fetching a single user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<UserEntity> user = userRepository.findById(id);

        if (user.isPresent()) {
            UserEntity userEntity = user.get();
            UserDTO userDTO = new UserDTO(
                    userEntity.getId(),
                    userEntity.getEmail(),
                    userEntity.getResetToken(),
                    userEntity.getCreatedAt(),
                    userEntity.getUpdatedAt());
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
