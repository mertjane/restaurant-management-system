package com.management.management_crm.dto;

public class MenuDTO {
    private Long id;
    private String name;
    private String description;
    private String photo_url;
    private String type;
    private Double price;
    private RestaurantDTO restaurant;

    public MenuDTO(Long id, String name, String description, String photo_url, String type, Double price,
            RestaurantDTO restaurant) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.photo_url = photo_url;
        this.type = type;
        this.price = price;
        this.restaurant = restaurant;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPhotoUrl() {
        return photo_url;
    }

    public String getType() {
        return type;
    }

    public Double getPrice() {
        return price;
    }

    public RestaurantDTO getRestaurant() {
        return restaurant;
    }
}