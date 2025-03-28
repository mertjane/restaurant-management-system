package com.management.management_crm.models;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookings")
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign Key Relation to Restaurant
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;
    
    @ManyToOne
    
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;
    

    // Column Annotations
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "time")
    private LocalTime time;

    @Column(name = "num_people")
    private int num_people;

    @Column(name = "status")
    private String status;

    // Getters, setters and constructors
    public BookingEntity() {
    }

    public BookingEntity(Long id, RestaurantEntity restaurant, CustomerEntity customer, LocalDate date,
            LocalTime time,
            String status, int num_people) {
        this.id = id;
        this.restaurant = restaurant;
        this.customer = customer;
        this.date = date;
        this.time = time;
        this.status = status;
        this.num_people = num_people;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RestaurantEntity getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantEntity restaurant) {
        this.restaurant = restaurant;
    }

    
    public CustomerEntity getCustomer() {
    return customer;
    }
    
    public void setCustomer(CustomerEntity customer) {
    this.customer = customer;
    }
    
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getNumPeople() {
        return num_people;
    }

    public void setNumPeople(int num_people) {
        this.num_people = num_people;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
