package com.management.management_crm.models;

import java.math.BigInteger;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Let DB generate the ID
    private Long id;

    // @Column Annotations

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private BigInteger phone;

    // Foreign Key Relation to Restaurant
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false) // Defines the FK column
    private RestaurantEntity restaurant;  // This links customers to restaurants

    // Getters, setters and constructors
    public CustomerEntity() {
    }

    public CustomerEntity(Long id, String name, String email, BigInteger phone, RestaurantEntity restaurant) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.restaurant = restaurant;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigInteger getPhone() {
        return phone;
    }

    public void setPhone(BigInteger phone) {
        this.phone = phone;
    }

    public RestaurantEntity getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantEntity restaurant) {
        this.restaurant = restaurant;
    }

}