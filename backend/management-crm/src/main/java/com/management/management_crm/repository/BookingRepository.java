package com.management.management_crm.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.management.management_crm.models.BookingEntity;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
        // Custom query to find bookings belongs to user's restaurant
        @Query("SELECT b FROM BookingEntity b WHERE b.restaurant.id = :restaurantId")
        Page<BookingEntity> findBookingsByRestaurantId(@Param("restaurantId") Long restaurantId, Pageable pageable);

        // Find Single Booking By ID
        Page<BookingEntity> findByRestaurantId(Long restaurantId, Pageable pageable);

        // Filter Restaurant's Bookings By ID with status= "Pending, Cancelled,
        // Confirmed"
        Page<BookingEntity> findByRestaurantIdAndStatus(Long restaurantId, String status, Pageable pageable);

        // Filter Restaurant's Bookings by ID with Date Range = "2025-04-03 -
        // 2025-01-03"
        Page<BookingEntity> findByRestaurantIdAndDateBetween(Long restaurantId, LocalDate startDate, LocalDate endDate,
                        Pageable pageable);

        // Filter Restaurant's Bookings by ID with Time Range = "09:00 - 21:00"
        Page<BookingEntity> findByRestaurantIdAndTimeBetween(Long restaurantId, LocalTime startTime, LocalTime endTime,
                        Pageable pageable);

        // Filter Restaurant's Bookings by ID with Status, Date and Time same time
        Page<BookingEntity> findByRestaurantIdAndStatusAndDateBetweenAndTimeBetween(
                        Long restaurantId, String status, LocalDate startDate, LocalDate endDate, LocalTime startTime,
                        LocalTime endTime, Pageable pageable);

        // Filter by a single Customer Name
        @Query("SELECT b FROM BookingEntity b WHERE b.restaurant.id = :restaurantId " +
                        "AND LOWER(b.customer.name) LIKE LOWER(CONCAT('%', :customerName, '%'))")
        Page<BookingEntity> findByRestaurantIdAndCustomerNameContaining(
                        @Param("restaurantId") Long restaurantId,
                        @Param("customerName") String customerName,
                        Pageable pageable);

        // Filter by multiple Customer Names
        @Query("SELECT b FROM BookingEntity b WHERE b.restaurant.id = :restaurantId " +
                        "AND LOWER(b.customer.name) IN :customerNames")
        Page<BookingEntity> findByRestaurantIdAndCustomerNames(
                        @Param("restaurantId") Long restaurantId,
                        @Param("customerNames") List<String> customerNames,
                        Pageable pageable);

        // Filter for exact customer name and status
        @Query("SELECT b FROM BookingEntity b " +
                        "WHERE b.restaurant.id = :restaurantId " +
                        "AND b.customer.name = :customerName " +
                        "AND b.status = :status")
        Page<BookingEntity> findByRestaurantIdAndCustomerNameAndStatus(
                        @Param("restaurantId") Long restaurantId,
                        @Param("customerName") String customerName,
                        @Param("status") String status,
                        Pageable pageable);

        // Search booking By restaurantId with customer name
        @Query("SELECT b FROM BookingEntity b WHERE b.restaurant.id = :restaurantId " +
                        "AND LOWER(b.customer.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
        Page<BookingEntity> searchByRestaurantIdAndCustomerName(
                        @Param("restaurantId") Long restaurantId,
                        @Param("searchTerm") String searchTerm,
                        Pageable pageable);

        @Query("SELECT b FROM BookingEntity b WHERE b.restaurant.id = :restaurantId AND b.date = :date")
        List<BookingEntity> findAllByRestaurantIdAndDate(
                        @Param("restaurantId") Long restaurantId,
                        @Param("date") LocalDate date);

        // Sort Bookings by date from today > future (custom sorting)
        Page<BookingEntity> findByRestaurantIdAndDateGreaterThanEqual(Long restaurantId, LocalDate date,
                        Pageable pageable);

}