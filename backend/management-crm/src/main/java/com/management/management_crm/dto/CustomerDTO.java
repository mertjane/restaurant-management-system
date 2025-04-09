package com.management.management_crm.dto;

import java.math.BigInteger;
import java.time.OffsetDateTime;

public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
    private BigInteger phone;
    private OffsetDateTime createdAt;
    private RestaurantDTO restaurant;

    public CustomerDTO(Long id, String name, String email, BigInteger phone, OffsetDateTime createdAt,
            RestaurantDTO restaurant) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
        this.restaurant = restaurant;
    }

    public CustomerDTO() {
    }

    // New constructor with minimal required fields (id and name)
    public CustomerDTO(Long id, String name, OffsetDateTime createdAt) {
        this(id, name, null, null, createdAt);
    }

    // New constructor with commonly used fields but without restaurant
    public CustomerDTO(Long id, String name, String email, BigInteger phone, OffsetDateTime createdAt) {
        this(id, name, email, phone, createdAt, null);
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public BigInteger getPhone() {
        return phone;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public RestaurantDTO getRestaurant() {
        return restaurant;
    }
}
