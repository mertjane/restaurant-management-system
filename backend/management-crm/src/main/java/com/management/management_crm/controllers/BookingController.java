package com.management.management_crm.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.management_crm.models.BookingEntity;
import com.management.management_crm.repository.BookingRepository;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingRepository bookingRepository;

    // @Get Method for fetching all bookings
    @GetMapping()
    public List<BookingEntity> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Endpoint to get bookings for a specific restaurant (e.g., restaurant ID = 1)
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<Page<BookingEntity>> getBookingsByRestaurant(
            @PathVariable Long restaurantId,
            Pageable pageable) {

        Page<BookingEntity> bookings = bookingRepository.findBookingsByRestaurantId(restaurantId, pageable);
        return ResponseEntity.ok(bookings);
    }
}
