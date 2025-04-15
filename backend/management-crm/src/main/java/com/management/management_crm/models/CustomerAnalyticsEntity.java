package com.management.management_crm.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "customer_analytics", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "restaurant_id", "date" })
})
public class CustomerAnalyticsEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;

  @Column(name = "restaurant_id", nullable = false)
  private Long restaurantId;

  @Column(nullable = false)
  private LocalDate date;

  @Column(name = "total_customers", nullable = false)
  private Long totalCusts;

  @Column(name = "total_customer_change_percent", nullable = false)
  private double totalCustsChange;

  @Column(name = "new_customers", nullable = false)
  private Long newCusts;

  @Column(name = "new_customer_change_percent", nullable = false)
  private double newCustsChange;

  @Column(nullable = false)
  private LocalDate peakDay;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  // --- Constructors ---
  public CustomerAnalyticsEntity() {
  }

  public CustomerAnalyticsEntity(UUID id, Long restaurantId, LocalDate date, Long totalCusts, double totalCustsChange,
  Long newCusts, double newCustsChange, LocalDate peakDay,
      LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.restaurantId = restaurantId;
    this.date = date;
    this.totalCusts = totalCusts;
    this.totalCustsChange = totalCustsChange;
    this.newCusts = newCusts;
    this.newCustsChange = newCustsChange;
    this.peakDay = peakDay;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
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

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

}
