package com.management.management_crm.dto;

public class RestaurantDTO {
    private Long id;
    private String name;
    private String websiteUrl;
    private UserDTO user; // Use UserDTO to avoid exposing password

    // Constructor
    public RestaurantDTO(Long id, String name, String websiteUrl, UserDTO user) {
        this.id = id;
        this.name = name;
        this.websiteUrl = websiteUrl;
        this.user = user;
    }

    // Constructor that accepts only id, name, and websiteUrl
    public RestaurantDTO(Long id, String name, String websiteUrl) {
        this.id = id;
        this.name = name;
        this.websiteUrl = websiteUrl;
        this.user = null; // Or assign a default value if needed
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public UserDTO getUser() {
        return user;
    }
}
