package com.management.management_crm.controllers;

import com.management.management_crm.dto.PasswordResetRequest;
import com.management.management_crm.models.UserEntity;
import com.management.management_crm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller // Change from @RestController to @Controller
public class EmailController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    /* Forgot Password - Sends reset link to email 
     * Step 1: We got email from page that user entered
     * Step 2: Function takes email and checks for is it valid or not if its valid generates random token ID with UUID
     * Step 3: Wrap the token in a link and send in the mail
     */
    @PostMapping("/auth/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> requestBody) {

        /* try and catch block for avoid server crash */
        try {
            String email = requestBody.get("email"); // Getting email from body

            if (email == null || email.isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }

            Optional<UserEntity> userOptional = userRepository.findByEmail(email); // Finds user with email 
            if (!userOptional.isPresent()) {
                return ResponseEntity.badRequest().body("No user found with this email");
            }

            UserEntity user = userOptional.get();

            // Generate reset token
            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            // Send email with reset link
            String resetLink = "http://localhost:8080/auth/reset-password?token=" + resetToken;
            sendResetEmail(email, resetLink);

            return ResponseEntity.ok("Password reset link sent to your email");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing forgot password: " + e.getMessage());
        }
    }

    /* Send Reset Email Function 
    *  Sends an email includes reset link
    */
    private void sendResetEmail(String toEmail, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Password Reset Request");
        message.setText("Click the link to reset your password: " + resetLink);
        message.setFrom("mck0391@gmail.com"); // Replace with your email
        mailSender.send(message);
    }

    /* Display Reset Password Form */
    @GetMapping("/auth/reset-password")
    public String showResetPasswordPage(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password"; // Renders reset-password.html
    }

    /*
     * Handle Password Reset Form Submission
     * Step 1: Takes token as a parameter from url and takes from body new password
     */
    @PostMapping("/auth/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestBody PasswordResetRequest request) {

        // try and catch block for avoid server crash
        try { 
            // Check inputs are they entered or not 
            if (request.getNewPassword() == null || request.getConfirmPassword() == null ||
                    request.getNewPassword().isEmpty() || request.getConfirmPassword().isEmpty()) {
                return ResponseEntity.badRequest().body("Both password fields are required.");
            }

            // Checks both passwords are match
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.badRequest().body("Passwords do not match.");
            }

            // Find user by reset token
            Optional<UserEntity> userOptional = userRepository.findByResetToken(token);
            if (!userOptional.isPresent()) {
                return ResponseEntity.badRequest().body("Invalid or expired reset token.");
            }

            
            UserEntity user = userOptional.get();
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            user.setResetToken(null); // Clear reset token
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            return ResponseEntity.ok("Password reset successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error resetting password: " + e.getMessage());
        }
    }

}
