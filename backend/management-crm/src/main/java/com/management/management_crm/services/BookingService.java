package com.management.management_crm.services;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.management.management_crm.dto.BookingDTO;

import org.springframework.data.domain.Pageable;

import com.management.management_crm.dto.CustomerDTO;
import com.management.management_crm.models.BookingEntity;
import com.management.management_crm.repository.BookingRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    // Get Booking by ID - Returns BookingDTO with only booking and customer details
    public BookingDTO getBookingById(Long id) {
        BookingEntity entity = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        // Create CustomerDTO with minimal details (assuming CustomerDTO has these
        // fields)
        CustomerDTO customerDTO = new CustomerDTO(
                entity.getCustomer().getId(),
                entity.getCustomer().getName(),
                entity.getCustomer().getEmail(),
                entity.getCustomer().getPhone());

        // Create BookingDTO without restaurant details
        return new BookingDTO(
                entity.getId(),
                entity.getDate(),
                entity.getTime(),
                entity.getNumPeople(),
                entity.getStatus(),
                null, // No restaurant details
                customerDTO);
    }

    // Update Booking by ID - Updates only booking fields
    public BookingDTO updateBookingById(Long id, BookingDTO bookingDTO) {
        BookingEntity entity = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));

        // Update only the fields that are provided (not null) in the incoming DTO
        if (bookingDTO.getDate() != null) {
            entity.setDate(bookingDTO.getDate());
        }
        if (bookingDTO.getTime() != null) {
            entity.setTime(bookingDTO.getTime());
        }
        if (bookingDTO.getNumPeople() != 0) { // Assuming 0 is not a valid number of people
            entity.setNumPeople(bookingDTO.getNumPeople());
        }
        if (bookingDTO.getStatus() != null) {
            entity.setStatus(bookingDTO.getStatus());
        }
        // Not updating customer or restaurant relationships as per your requirement

        BookingEntity updatedEntity = bookingRepository.save(entity);

        // Create CustomerDTO with all customer fields we want to return
        CustomerDTO customerDTO = new CustomerDTO(
                updatedEntity.getCustomer().getId(),
                updatedEntity.getCustomer().getName(),
                updatedEntity.getCustomer().getEmail(),
                updatedEntity.getCustomer().getPhone());

        // Return updated DTO without restaurant details
        return new BookingDTO(
                updatedEntity.getId(),
                updatedEntity.getDate(),
                updatedEntity.getTime(),
                updatedEntity.getNumPeople(),
                updatedEntity.getStatus(),
                null, // No restaurant details
                customerDTO);
    }

    // Filter Service Method
    public Page<BookingDTO> filterBookings(Long restaurantId, String status, String startTime, String endTime,
            String startDate, String endDate, List<String> customerNames, Pageable pageable) {
        Page<BookingEntity> bookings;

        if (customerNames != null && !customerNames.isEmpty()) {
            List<String> lowerCaseNames = customerNames.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());

            if (lowerCaseNames.size() == 1) {
                if (status != null) {
                    bookings = bookingRepository.findByRestaurantIdAndCustomerNameAndStatus(
                            restaurantId, customerNames.get(0), status, pageable); // Use exact match
                } else {
                    bookings = bookingRepository.findByRestaurantIdAndCustomerNameContaining(
                            restaurantId, lowerCaseNames.get(0), pageable);
                }
            } else {
                bookings = bookingRepository.findByRestaurantIdAndCustomerNames(
                        restaurantId, lowerCaseNames, pageable);
            }
        } else if (status != null && startDate != null && endDate != null && startTime != null && endTime != null) {
            bookings = bookingRepository.findByRestaurantIdAndStatusAndDateBetweenAndTimeBetween(
                    restaurantId, status, LocalDate.parse(startDate), LocalDate.parse(endDate),
                    LocalTime.parse(startTime), LocalTime.parse(endTime), pageable);
        } else if (status != null) {
            bookings = bookingRepository.findByRestaurantIdAndStatus(restaurantId, status, pageable);
        } else if (startDate != null && endDate != null) {
            bookings = bookingRepository.findByRestaurantIdAndDateBetween(restaurantId, LocalDate.parse(startDate),
                    LocalDate.parse(endDate), pageable);
        } else if (startTime != null && endTime != null) {
            bookings = bookingRepository.findByRestaurantIdAndTimeBetween(restaurantId, LocalTime.parse(startTime),
                    LocalTime.parse(endTime), pageable);
        } else {
            bookings = bookingRepository.findByRestaurantId(restaurantId, pageable);
        }

        return bookings.map(booking -> new BookingDTO(
                booking.getId(),
                booking.getDate(),
                booking.getTime(),
                booking.getNumPeople(),
                booking.getStatus(),
                null, // Exclude restaurant info
                new CustomerDTO(
                        booking.getCustomer().getId(),
                        booking.getCustomer().getName(),
                        booking.getCustomer().getEmail(),
                        booking.getCustomer().getPhone())));
    }

    // New search method
    public Page<BookingDTO> searchBookings(Long restaurantId, String searchTerm, Pageable pageable) {
        Page<BookingEntity> bookings;

        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            bookings = bookingRepository.searchByRestaurantIdAndCustomerName(
                    restaurantId, searchTerm.trim(), pageable);
        } else {
            bookings = bookingRepository.findByRestaurantId(restaurantId, pageable);
        }

        return bookings.map(booking -> new BookingDTO(
                booking.getId(),
                booking.getDate(),
                booking.getTime(),
                booking.getNumPeople(),
                booking.getStatus(),
                null,
                new CustomerDTO(
                        booking.getCustomer().getId(),
                        booking.getCustomer().getName(),
                        booking.getCustomer().getEmail(),
                        booking.getCustomer().getPhone())));
    }

}
