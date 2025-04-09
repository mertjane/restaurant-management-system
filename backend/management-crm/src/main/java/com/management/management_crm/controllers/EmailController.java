package com.management.management_crm.controllers;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.management.management_crm.dto.PasswordResetRequest;
import com.management.management_crm.models.UserEntity;
import com.management.management_crm.repository.UserRepository;

@Controller // Change from @RestController to @Controller
public class EmailController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    /*
     * Forgot Password - Sends reset link to email
     * Step 1: We got email from page that user entered
     * Step 2: Function takes email and checks for is it valid or not if its valid
     * generates random token ID with UUID
     * Step 3: Wrap the token in a link and send in the mail
     */
    @PostMapping("/auth/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> requestBody) {

        /* try and catch block for avoid server crash */
        try {
            String email = requestBody.get("email"); // Getting email from body

            if (email == null || email.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Email is required"));
            }

            Optional<UserEntity> userOptional = userRepository.findByEmail(email); // Finds user with email
            if (!userOptional.isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "No user found with this email"));
            }

            UserEntity user = userOptional.get();

            // Generate reset token
            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            // Send email with reset link
            String resetLink = "http://localhost:5173/login?token=" + resetToken;
            sendResetEmail(email, resetLink);

            return ResponseEntity.ok(Map.of("success", true, "message", "Password reset link sent to your email"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing forgot password: " + e.getMessage());
        }
    }

    /*
     * Send Reset Email Function
     * Sends an email includes reset link
     */
    private void sendResetEmail(String toEmail, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Password Reset Request");
        message.setText("Click the link to reset your password: " + resetLink);
        message.setFrom("mck0391@gmail.com"); // Replace with your email
        mailSender.send(message);
    }

    /*
     * Send Booking Confirmation Email Function
     */
    public void sendBookingStatus(String toEmail, String message) {
        SimpleMailMessage bookingMessage = new SimpleMailMessage();
        bookingMessage.setTo(toEmail);
        bookingMessage.setSubject("Booking Status");
        bookingMessage.setText(message);
        bookingMessage.setFrom("mck0391@gmail.com");
        mailSender.send(bookingMessage);
    }

    /*
     * Send Booking Request taken email Function
     */
    public void sendAutoReply(String toEmail, String message) {
        SimpleMailMessage autoMessage = new SimpleMailMessage();
        autoMessage.setTo(toEmail);
        autoMessage.setSubject("Booking Request Taken");
        autoMessage.setText(message);
        autoMessage.setFrom("mck0391@gmail.com"); // Replace with restaurant email
        mailSender.send(autoMessage);
    }

    /* Display Reset Password Form */
    /*
     * @GetMapping("/auth/reset-password")
     * public String showResetPasswordPage(@RequestParam String token, Model model)
     * {
     * model.addAttribute("token", token);
     * return "reset-password.html"; // Renders reset-password.html
     * }
     */

    /* Display Reset Password Form */

    @GetMapping("/auth/reset-password")
    public ResponseEntity<Map<String, Object>> showResetPasswordPage(@RequestParam String token) {
        Optional<UserEntity> userOptional = userRepository.findByResetToken(token);
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("success", false, "message", "Invalid or expired reset token"));
        }

        // Return JSON response instead of redirect
        return ResponseEntity.ok(Map.of("success", true, "message", "Token is valid"));
    }

    /*
     * Handle Password Reset Form Submission
     * Step 1: Takes token as a parameter from url and takes from body new password
     */
    @PostMapping("/auth/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(
            @RequestParam String token,
            @RequestBody PasswordResetRequest request) {

        // try and catch block for avoid server crash
        try {
            // Check inputs are they entered or not
            if (request.getNewPassword() == null || request.getConfirmPassword() == null ||
                    request.getNewPassword().isEmpty() || request.getConfirmPassword().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Both password fields are required."));
            }

            // Checks both passwords are match
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Passwords do not match."));
            }

            // Find user by reset token
            Optional<UserEntity> userOptional = userRepository.findByResetToken(token);
            if (!userOptional.isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("success", false, "message", "Invalid or expired reset token."));
            }

            UserEntity user = userOptional.get();
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            user.setResetToken(null); // Clear reset token
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            return ResponseEntity.ok(Map.of("success", true, "message", "Password reset successfully."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("success", false, "message", "Error resetting password: " + e.getMessage()));
        }
    }

}
