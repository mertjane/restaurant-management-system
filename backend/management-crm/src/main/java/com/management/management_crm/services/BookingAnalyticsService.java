package com.management.management_crm.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.management.management_crm.dto.BookingAnalyticsDTO;
import com.management.management_crm.models.BookingAnalyticsEntity;
import com.management.management_crm.models.BookingEntity;
import com.management.management_crm.repository.BookingAnalyticsRepository;
import com.management.management_crm.repository.BookingRepository;

import jakarta.transaction.Transactional;

@Service
public class BookingAnalyticsService {

    private final BookingAnalyticsRepository repository;
    private final BookingRepository bookingRepository;

    public BookingAnalyticsService(BookingAnalyticsRepository repository, BookingRepository bookingRepository) {
        this.repository = repository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public BookingAnalyticsDTO computeAndSaveAnalytics(Long restaurantId, LocalDate date) {
        List<BookingEntity> bookings = bookingRepository.findAllByRestaurantIdAndDate(restaurantId, date);

        if (bookings.isEmpty()) {
            return null; // Or return a DTO with zeros
        }

        int total = bookings.size();
        BigDecimal avgSize = BigDecimal.valueOf(bookings.stream()
                .mapToInt(BookingEntity::getNumPeople)
                .average().orElse(0));

        int cancelled = (int) bookings.stream().filter(b -> b.getStatus().equalsIgnoreCase("Cancelled")).count();
        int confirmed = (int) bookings.stream().filter(b -> b.getStatus().equalsIgnoreCase("Confirmed")).count();
        int pending = (int) bookings.stream().filter(b -> b.getStatus().equalsIgnoreCase("Pending")).count();

        Map<LocalTime, Long> timeCounts = bookings.stream()
                .collect(Collectors.groupingBy(BookingEntity::getTime, Collectors.counting()));
        LocalTime peakHour = Collections.max(timeCounts.entrySet(), Map.Entry.comparingByValue()).getKey();

        BookingAnalyticsEntity entity = new BookingAnalyticsEntity();
        entity.setRestaurantId(restaurantId);
        entity.setDate(date);
        entity.setTotalBookings(total);
        entity.setAvgPartySize(avgSize);
        entity.setCancelledCount(cancelled);
        entity.setConfirmedCount(confirmed);
        entity.setPendingCount(pending);
        entity.setPeakHour(peakHour);

        repository.findByRestaurantIdAndDate(restaurantId, date).ifPresent(existing -> {
            entity.setId(existing.getId()); // Overwrite if already exists
        });

        BookingAnalyticsEntity saved = repository.save(entity);

        return new BookingAnalyticsDTO(
                saved.getRestaurantId(),
                saved.getDate(),
                saved.getTotalBookings(),
                saved.getAvgPartySize(),
                saved.getCancelledCount(),
                saved.getConfirmedCount(),
                saved.getPendingCount(),
                saved.getPeakHour()
        );
    }

    public List<BookingAnalyticsDTO> getAnalyticsBetween(Long restaurantId, LocalDate start, LocalDate end) {
        return repository.findAllByRestaurantIdAndDateBetween(restaurantId, start, end)
                .stream()
                .map(a -> new BookingAnalyticsDTO(
                        a.getRestaurantId(), a.getDate(), a.getTotalBookings(),
                        a.getAvgPartySize(), a.getCancelledCount(), a.getConfirmedCount(),
                        a.getPendingCount(), a.getPeakHour()))
                .collect(Collectors.toList());
    }
}
