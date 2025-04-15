package com.management.management_crm.services;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.management.management_crm.models.BookingEntity;
import com.management.management_crm.models.CustomerAnalyticsEntity;
import com.management.management_crm.models.CustomerEntity;
import com.management.management_crm.repository.BookingRepository;
import com.management.management_crm.repository.CustomerAnalyticsRepository;
import com.management.management_crm.repository.CustomerRepository;

@Service
public class CustomerAnalyticsService {

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private BookingRepository bookingRepository;

  @Autowired
  private CustomerAnalyticsRepository customerAnalyticsRepository;

  // Get Customer Analytics by restaurantId and date
  public Optional<CustomerAnalyticsEntity> getCustomerAnalytics(Long restaurantId, YearMonth currentMonth) {
    LocalDate analyticsDate = currentMonth.atDay(1); // Use the first day of the month for tracking
    return customerAnalyticsRepository.findByRestaurantIdAndDate(restaurantId, analyticsDate);
  }

  // Calculate Customer Analytics based on restaurant ID and current month
  public void calculateCustomerAnalytics(Long restaurantId, YearMonth currentMonth) {
    // Call the method to update analytics after a booking is made
    updateAnalyticsAfterBooking(restaurantId, currentMonth);
  }

  // Update Analytics After Booking (Calculates all analytics data)
  public void updateAnalyticsAfterBooking(Long restaurantId, YearMonth currentMonth) {
    YearMonth lastMonth = currentMonth.minusMonths(1);

    // Total customers for this restaurant
    Long totalCustomers = customerRepository.countByRestaurantId(restaurantId);

    // Get start and end of current and last month as OffsetDateTime
    OffsetDateTime startOfCurrentMonth = currentMonth.atDay(1).atStartOfDay().atOffset(ZoneOffset.UTC);
    OffsetDateTime endOfCurrentMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59).atOffset(ZoneOffset.UTC);
    OffsetDateTime startOfLastMonth = lastMonth.atDay(1).atStartOfDay().atOffset(ZoneOffset.UTC);
    OffsetDateTime endOfLastMonth = lastMonth.atEndOfMonth().atTime(23, 59, 59).atOffset(ZoneOffset.UTC);

    // New customers this month
    long newCustomersThisMonth = customerRepository.countByRestaurantIdAndCreatedAtBetween(
        restaurantId, startOfCurrentMonth, endOfCurrentMonth);

    // New customers last month
    long newCustomersLastMonth = customerRepository.countByRestaurantIdAndCreatedAtBetween(
        restaurantId, startOfLastMonth, endOfLastMonth);

    // Calculate percentage change for new customers
    double newCustomerChange = calculatePercentageChange(newCustomersLastMonth, newCustomersThisMonth);

    // Calculate the peak day for bookings this month
    LocalDate peakDayThisMonth = calculatePeakBookingDay(restaurantId, currentMonth);

    LocalDate analyticsDate = currentMonth.atDay(1); // Use the first day of the month to track

    // Check if analytics for the month already exist
    Optional<CustomerAnalyticsEntity> existingAnalyticsOpt = customerAnalyticsRepository
        .findByRestaurantIdAndDate(restaurantId, analyticsDate);

    // Create or update the CustomerAnalyticsEntity object
    CustomerAnalyticsEntity analytics = existingAnalyticsOpt.orElseGet(CustomerAnalyticsEntity::new);
    analytics.setRestaurantId(restaurantId);
    analytics.setDate(analyticsDate);
    analytics.setTotalCusts(totalCustomers); // Set total customers (cast to int)
    analytics.setNewCusts(newCustomersThisMonth); // Set new customers (cast to int)
    analytics.setTotalCustsChange(
        calculatePercentageChange(getTotalCustomersLastMonth(restaurantId, lastMonth), totalCustomers));
    analytics.setNewCustsChange(newCustomerChange);
    analytics.setPeakDay(peakDayThisMonth);

    // Save the analytics data to the repository
    customerAnalyticsRepository.save(analytics);
  }

  // Method to calculate the percentage change between two values
  private double calculatePercentageChange(long previous, long current) {
    if (previous == 0 && current == 0) {
      return 0.0; // No change if both are zero
    }
    if (previous == 0) {
      // When previous is zero, this is technically an infinite increase
      // You can either return a specific value to indicate this special case
      // or return the actual current value as a percentage increase
      return 100.0 * current; // Shows actual growth instead of capping
    }

    // Standard percentage change calculation
    double change = ((double) (current - previous) / previous) * 100.0;

    // Return the actual change without capping
    return change;
  }

  // Method to get total customers from last month for comparison
  private long getTotalCustomersLastMonth(Long restaurantId, YearMonth lastMonth) {
    OffsetDateTime startOfLastMonth = lastMonth.atDay(1).atStartOfDay().atOffset(ZoneOffset.UTC);
    OffsetDateTime endOfLastMonth = lastMonth.atEndOfMonth().atTime(23, 59, 59).atOffset(ZoneOffset.UTC);
    return customerRepository.countByRestaurantIdAndCreatedAtBetween(restaurantId, startOfLastMonth, endOfLastMonth);
  }

  // Method to calculate the peak booking day (most bookings on a given day) for
  // the current month
  private LocalDate calculatePeakBookingDay(Long restaurantId, YearMonth currentMonth) {
    Pageable pageable = PageRequest.of(0, 1000, Sort.by("date").ascending());
    Page<BookingEntity> bookingsPage = bookingRepository.findByRestaurantId(restaurantId, pageable);
    List<BookingEntity> bookings = bookingsPage.getContent();

    Map<LocalDate, Long> dayCounts = bookings.stream()
        .filter(b -> YearMonth.from(b.getDate()).equals(currentMonth))
        .collect(Collectors.groupingBy(BookingEntity::getDate, Collectors.counting()));

    // Return the date with the highest number of bookings
    return dayCounts.entrySet()
        .stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey)
        .orElse(currentMonth.atDay(1)); // Fallback if no bookings
  }
}
