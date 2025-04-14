package com.management.management_crm.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.management.management_crm.controllers.EmailController;
import com.management.management_crm.dto.BookingDTO;
import com.management.management_crm.dto.CustomerDTO;
import com.management.management_crm.dto.RestaurantDTO;
import com.management.management_crm.models.BookingEntity;
import com.management.management_crm.models.CustomerEntity;
import com.management.management_crm.models.RestaurantEntity;
import com.management.management_crm.repository.BookingRepository;
import com.management.management_crm.repository.CustomerRepository;
import com.management.management_crm.repository.RestaurantRepository;

import jakarta.transaction.Transactional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmailController emailController;

    // Create Booking
    @Transactional
    public BookingDTO createBooking(Long restaurantId, BookingDTO bookingDTO) {
        // Fetch the restaurant based on restaurantId from the params
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // Create the customer and associate it with the restaurant
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setName(bookingDTO.getCustomer().getName());
        customerEntity.setEmail(bookingDTO.getCustomer().getEmail());
        customerEntity.setPhone(bookingDTO.getCustomer().getPhone());
        customerEntity.setRestaurant(restaurant); // Ensure restaurant is set

        // Manually set createdAt if not using @CreationTimestamp
        if (customerEntity.getCreatedAt() == null) {
            customerEntity.setCreatedAt(OffsetDateTime.now()); // Set the current time
        }

        // Save the customer entity
        customerRepository.save(customerEntity);

        // Now, create the booking and associate it with the customer and restaurant
        BookingEntity bookingEntity = new BookingEntity();
        bookingEntity.setDate(bookingDTO.getDate());
        bookingEntity.setTime(bookingDTO.getTime());
        bookingEntity.setNumPeople(bookingDTO.getNumPeople());
        bookingEntity.setStatus(bookingDTO.getStatus());
        bookingEntity.setCustomer(customerEntity);
        bookingEntity.setRestaurant(restaurant);

        // Save the booking entity
        bookingRepository.save(bookingEntity);

        // Send Auto Email to customer
        String customerEmail = bookingEntity.getCustomer().getEmail();
        String message = String.format(
                "Dear %s, \n\nThank you for your booking request. We will get back to you shortly.\n\nBest regards,\n%s",
                bookingEntity.getCustomer().getName(), bookingEntity.getRestaurant().getName());
        emailController.sendAutoReply(customerEmail, message);

        // Convert the saved entities to DTOs and return the response
        return new BookingDTO(
                bookingEntity.getId(),
                bookingEntity.getDate(),
                bookingEntity.getTime(),
                bookingEntity.getNumPeople(),
                bookingEntity.getStatus(),
                new RestaurantDTO(restaurant.getId(), restaurant.getName(), restaurant.getWebsiteUrl()),
                new CustomerDTO(customerEntity.getId(), customerEntity.getName(), customerEntity.getEmail(),
                        customerEntity.getPhone(), customerEntity.getCreatedAt()));
    }

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
                entity.getCustomer().getPhone(),
                entity.getCustomer().getCreatedAt());

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

        // Store old status for comparison
        String oldStatus = entity.getStatus();

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

        // Check if status was changed to Confirmed or Cancelled
        if (bookingDTO.getStatus() != null && !bookingDTO.getStatus().equals(oldStatus)) {
            String customerEmail = updatedEntity.getCustomer().getEmail();
            String status = updatedEntity.getStatus();

            if ("Confirmed".equals(status)) {
                String message = String.format(
                        "Dear %s, \n\nYour booking request has been confirmed. See you on %s at %s. We look forward to serving you!\n\nBest regards,\n%s",
                        updatedEntity.getCustomer().getName(), updatedEntity.getDate(), updatedEntity.getTime(),
                        updatedEntity.getRestaurant().getName());
                emailController.sendBookingStatus(customerEmail, message);
            } else if ("Cancelled".equals(status)) {
                String message = String.format(
                        "Dear %s, \n\nYour booking has been cancelled. If this was a mistake or you need assistance, please contact us.\n\nBest regards,\n%s",
                        updatedEntity.getCustomer().getName(), updatedEntity.getRestaurant().getName());
                emailController.sendBookingStatus(customerEmail, message);

            }
        }

        // Create CustomerDTO with all customer fields we want to return
        CustomerDTO customerDTO = new CustomerDTO(
                updatedEntity.getCustomer().getId(),
                updatedEntity.getCustomer().getName(),
                updatedEntity.getCustomer().getEmail(),
                updatedEntity.getCustomer().getPhone(),
                updatedEntity.getCustomer().getCreatedAt());

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
                        booking.getCustomer().getPhone(),
                        booking.getCustomer().getCreatedAt())));
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
                        booking.getCustomer().getPhone(),
                        booking.getCustomer().getCreatedAt())));
    }

}
