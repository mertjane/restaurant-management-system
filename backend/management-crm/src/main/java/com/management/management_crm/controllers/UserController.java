package com.management.management_crm.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.management_crm.dto.UserDTO;
import com.management.management_crm.models.UserEntity;
import com.management.management_crm.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long expirationTime;

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


    // Get logged-in user by token // alternative method // 
    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInUser(HttpServletRequest request) {
        try {
            // Get JWT from the Authorization header
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
            }
            
            String token = authHeader.substring(7); // Remove "Bearer " prefix

            // Parse token to get claims
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject(); // Extract email from token
            
            // Find user by email
            Optional<UserEntity> userOptional = userRepository.findByEmail(email);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(404).body("User not found");
            }

            UserEntity user = userOptional.get();

            // Return user data (excluding sensitive fields)
            UserDTO userDTO = new UserDTO(
                    user.getId(),
                    user.getEmail(),
                    user.getResetToken(),
                    user.getCreatedAt(),
                    user.getUpdatedAt());

            return ResponseEntity.ok(userDTO);

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid or expired token: " + e.getMessage());
        }
    }
}
