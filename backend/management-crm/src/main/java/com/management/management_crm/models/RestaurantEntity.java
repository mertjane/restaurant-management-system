package com.management.management_crm.models;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "restaurants")
public class RestaurantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Let DB generate the ID
    private Long id;

    // @Column Annotations

    @Column(name = "name")
    private String name;

    @Column(name = "website_url")
    private String website_url;

    // Foreign Key Relation to User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Defines the FK column
    private UserEntity user;


    // Getters, setters and constructors
    public RestaurantEntity() {
    }

    public RestaurantEntity(Long id, String name, String website_url, UserEntity user) {
        this.id = id;
        this.name = name;
        this.website_url = website_url;
        this.user = user;
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

    public String getWebsiteUrl() {
        return website_url;
    }

    public void setWebsiteUrl(String website_url) {
        this.website_url = website_url;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
