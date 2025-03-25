package com.management.management_crm.dto;

import java.time.LocalDateTime;

public class UserDTO {
    private Long id;
    private String email;
    private String resetToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor
    public UserDTO(Long id, String email, String resetToken, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.resetToken = resetToken;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getResetToken() { return resetToken; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}

