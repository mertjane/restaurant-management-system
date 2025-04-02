package com.management.management_crm.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingDTO {
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private int num_people;
    private String status;
    private RestaurantDTO restaurant;
    private CustomerDTO customer;

    public BookingDTO(Long id, LocalDate date, LocalTime time, int num_people, String status, RestaurantDTO restaurant,
            CustomerDTO customer) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.num_people = num_people;
        this.status = status;
        this.restaurant = restaurant;
        this.customer = customer;
    }

    

    // Getters
    public Long getID() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getNumPeople() {
        return num_people;
    }

    public String getStatus() {
        return status;
    }

    public RestaurantDTO getRestaurant() {
        return restaurant;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }
}
