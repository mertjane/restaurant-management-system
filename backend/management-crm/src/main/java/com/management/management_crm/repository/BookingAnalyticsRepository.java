package com.management.management_crm.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.management.management_crm.models.BookingAnalyticsEntity;

@Repository
public interface BookingAnalyticsRepository extends JpaRepository<BookingAnalyticsEntity, UUID> {
  Optional<BookingAnalyticsEntity> findByRestaurantIdAndDate(Long restaurantId, LocalDate date);

  List<BookingAnalyticsEntity> findAllByRestaurantIdAndDateBetween(Long restaurantId, LocalDate start, LocalDate end);
}
