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
@Table(name = "booking_analytics", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "restaurant_id", "date" })
})
public class BookingAnalyticsEntity {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;

  @Column(name = "restaurant_id", nullable = false)
  private Long restaurantId;

  @Column(nullable = false)
  private LocalDate date;

  @Column(name = "total_bookings", nullable = false)
  private int totalBookings;

  @Column(name = "avg_party_size", nullable = false)
  private BigDecimal avgPartySize;

  @Column(name = "cancelled_count", nullable = false)
  private int cancelledCount;

  @Column(name = "confirmed_count", nullable = false)
  private int confirmedCount;

  @Column(name = "pending_count", nullable = false)
  private int pendingCount;

  @Column(name = "peak_hour", nullable = false)
  private LocalTime peakHour;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  // --- Constructors ---
  public BookingAnalyticsEntity() {
  }

  public BookingAnalyticsEntity(UUID id, Long restaurantId, LocalDate date, int totalBookings,
      BigDecimal avgPartySize, int cancelledCount, int confirmedCount,
      int pendingCount, LocalTime peakHour,
      LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.restaurantId = restaurantId;
    this.date = date;
    this.totalBookings = totalBookings;
    this.avgPartySize = avgPartySize;
    this.cancelledCount = cancelledCount;
    this.confirmedCount = confirmedCount;
    this.pendingCount = pendingCount;
    this.peakHour = peakHour;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  // --- Getters and Setters ---
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

  public int getTotalBookings() {
    return totalBookings;
  }

  public void setTotalBookings(int totalBookings) {
    this.totalBookings = totalBookings;
  }

  public BigDecimal getAvgPartySize() {
    return avgPartySize;
  }

  public void setAvgPartySize(BigDecimal avgPartySize) {
    this.avgPartySize = avgPartySize;
  }

  public int getCancelledCount() {
    return cancelledCount;
  }

  public void setCancelledCount(int cancelledCount) {
    this.cancelledCount = cancelledCount;
  }

  public int getConfirmedCount() {
    return confirmedCount;
  }

  public void setConfirmedCount(int confirmedCount) {
    this.confirmedCount = confirmedCount;
  }

  public int getPendingCount() {
    return pendingCount;
  }

  public void setPendingCount(int pendingCount) {
    this.pendingCount = pendingCount;
  }

  public LocalTime getPeakHour() {
    return peakHour;
  }

  public void setPeakHour(LocalTime peakHour) {
    this.peakHour = peakHour;
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
