package com.management.management_crm.controllers;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.management_crm.dto.LoginRequest;
import com.management.management_crm.dto.UserDTO;
import com.management.management_crm.models.UserEntity;
import com.management.management_crm.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // For hashing passwords

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long expirationTime;

    /* @Post Register User (restaurant admin) to db */
    @PostMapping("/register")
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

    /* @POST Method Login User */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            String email = loginRequest.getEmail();
            String password = loginRequest.getPassword();

            // Validate input
            if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Email and password are required"));
            }

            // Find user by email
            Optional<UserEntity> userOptional = userRepository.findByEmail(email);
            if (!userOptional.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid email or password"));
            }

            UserEntity user = userOptional.get();

            // Verify password
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid email or password"));
            }

            // Generate JWT token
            String token = Jwts.builder()
                    .setSubject(user.getEmail())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                    .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
                    .compact();

            // Create user response DTO (excluding sensitive fields like password)
            UserDTO userDTO = new UserDTO(
                    user.getId(),
                    user.getEmail(),
                    user.getResetToken(),
                    user.getCreatedAt(),
                    user.getUpdatedAt());

            // Return token and user info in response
            return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "token", token,
                    "user", userDTO));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error logging in: " + e.getMessage()));
        }
    }
}