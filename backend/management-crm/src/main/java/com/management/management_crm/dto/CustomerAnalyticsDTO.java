package com.management.management_crm.dto;

import java.time.LocalDate;
import java.util.UUID;

public class CustomerAnalyticsDTO {
    private UUID id;
    private Long restaurantId;
    private LocalDate date;

    private Long totalCusts;
    private Long newCusts;

    private double totalCustsChange; // Compared to last month
    private double newCustsChange; // Compared to last month

    private LocalDate peakDay;

    // Constructors
    public CustomerAnalyticsDTO() {
    }

    public CustomerAnalyticsDTO(UUID id, Long restaurantId, LocalDate date, Long totalCusts, Long newCusts,
            double totalCustsChange, double newCustsChange,
            LocalDate peakDay) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.date = date;
        this.totalCusts = totalCusts;
        this.newCusts = newCusts;
        this.totalCustsChange = totalCustsChange;
        this.newCustsChange = newCustsChange;
        this.peakDay = peakDay;
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getTotalCusts() {
        return totalCusts;
    }

    public void setTotalCusts(Long totalCusts) {
        this.totalCusts = totalCusts;
    }

    public Long getNewCusts() {
        return newCusts;
    }

    public void setNewCusts(Long newCusts) {
        this.newCusts = newCusts;
    }

    public double getTotalCustsChange() {
        return totalCustsChange;
    }

    public void setTotalCustsChange(double totalCustsChange) {
        this.totalCustsChange = totalCustsChange;
    }

    public double getNewCustsChange() {
        return newCustsChange;
    }

    public void setNewCustsChange(double newCustsChange) {
        this.newCustsChange = newCustsChange;
    }

    public LocalDate getPeakDay() {
        return peakDay;
    }

    public void setPeakDay(LocalDate peakDay) {
        this.peakDay = peakDay;
    }
}
