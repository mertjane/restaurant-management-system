package com.management.management_crm.dto;

import java.math.BigInteger;

public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
    private BigInteger phone;
    private RestaurantDTO restaurant;

    public CustomerDTO(Long id, String name, String email, BigInteger phone, RestaurantDTO restaurant) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.restaurant = restaurant;
    }

    // New constructor with minimal required fields (id and name)
    public CustomerDTO(Long id, String name) {
        this(id, name, null, null, null);
    }

    // New constructor with commonly used fields but without restaurant
    public CustomerDTO(Long id, String name, String email, BigInteger phone) {
        this(id, name, email, phone, null);
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

    public RestaurantDTO getRestaurant() {
        return restaurant;
    }
}
