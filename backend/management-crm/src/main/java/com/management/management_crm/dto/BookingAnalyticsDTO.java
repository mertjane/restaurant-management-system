package com.management.management_crm.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class BookingAnalyticsDTO {
  private Long restaurantId;
  private LocalDate date;
  private int totalBookings;
  private BigDecimal totalCust;
  private int cancelledCount;
  private int confirmedCount;
  private int pendingCount;
  private LocalTime peakHour;

  // --- Constructors ---
  public BookingAnalyticsDTO() {
  }

  public BookingAnalyticsDTO(Long restaurantId, LocalDate date, int totalBookings,
      BigDecimal totalCust, int cancelledCount, int confirmedCount,
      int pendingCount, LocalTime peakHour) {
    this.restaurantId = restaurantId;
    this.date = date;
    this.totalBookings = totalBookings;
    this.totalCust = totalCust;
    this.cancelledCount = cancelledCount;
    this.confirmedCount = confirmedCount;
    this.pendingCount = pendingCount;
    this.peakHour = peakHour;
  }

  // --- Getters and Setters ---
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

  public BigDecimal getTotalCust() {
    return totalCust;
  }

  public void setTotalCust(BigDecimal totalCust) {
    this.totalCust = totalCust;
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

}
