package com.management.management_crm.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.management.management_crm.dto.BookingDTO;

import com.management.management_crm.models.BookingEntity;
import com.management.management_crm.repository.BookingRepository;
import com.management.management_crm.services.BookingService;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

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

    // Get single Booking By ID
    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    // Update Booking By ID
    @PatchMapping("/{id}")
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable Long id, @RequestBody BookingDTO bookingDTO) {
        return ResponseEntity.ok(bookingService.updateBookingById(id, bookingDTO));
    }

    @GetMapping("/restaurant/{restaurantId}/filter")
    public ResponseEntity<Page<BookingDTO>> filterBookings(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String customerNames,
            Pageable pageable) {

        List<String> customerNameList = (customerNames != null && !customerNames.isEmpty())
                ? Arrays.stream(customerNames.split(","))
                        .map(name -> name.replace("+", " ")) // Replace + with space
                        .map(String::trim) // Still trim any extra spaces
                        .collect(Collectors.toList())
                : null;

                

        Page<BookingDTO> filteredBookings = bookingService.filterBookings(
                restaurantId, status, startTime, endTime, startDate, endDate, customerNameList, pageable);

        return ResponseEntity.ok(filteredBookings);
    }

    // New search endpoint
    @GetMapping("/restaurant/{restaurantId}/search")
    public ResponseEntity<Page<BookingDTO>> searchBookings(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) String searchTerm,
            Pageable pageable) {

        Page<BookingDTO> searchResults = bookingService.searchBookings(restaurantId, searchTerm, pageable);
        return ResponseEntity.ok(searchResults);
    }

}
