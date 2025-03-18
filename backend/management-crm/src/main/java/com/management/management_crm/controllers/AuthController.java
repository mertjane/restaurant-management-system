package com.management.management_crm.controllers;

import com.management.management_crm.models.UserEntity;
import com.management.management_crm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // For hashing passwords


    /* @Post Register User (restaurant admin) to db  */
    @PostMapping("/auth/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> requestBody) {
        try {
            // Extract email and password from request body
            String email = requestBody.get("email");
            String password = requestBody.get("password");

            // Validate input
            if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                return ResponseEntity.badRequest().body("Email and password are required");
            }

            // Check if email already exists
            if (userRepository.findByEmail(email).isPresent()) {
                return ResponseEntity.badRequest().body("Email already in use");
            }

            // Create new user
            UserEntity user = new UserEntity();
            
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password)); // Hash the password
            user.setResetToken(null); // No reset token initially
            user.setCreatedAt(LocalDateTime.now()); // Set creation time
            user.setUpdatedAt(LocalDateTime.now()); // Set update time

            // Save to database
            userRepository.save(user);

            // Return success response
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error registering user: " + e.getMessage());
        }
    }

}