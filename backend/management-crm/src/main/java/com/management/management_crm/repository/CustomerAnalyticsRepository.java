package com.management.management_crm.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.management.management_crm.models.CustomerAnalyticsEntity;

@Repository
public interface CustomerAnalyticsRepository extends JpaRepository<CustomerAnalyticsEntity, UUID> {

  // Find analytics record for a specific restaurant and month
  Optional<CustomerAnalyticsEntity> findByRestaurantIdAndDate(Long restaurantId, LocalDate date);

  // Get all analytics for a restaurant sorted by date
  List<CustomerAnalyticsEntity> findByRestaurantIdOrderByDateAsc(Long restaurantId);

  // Check if an analytics entry already exists for given restaurant and date
  boolean existsByRestaurantIdAndDate(Long restaurantId, LocalDate date);
}
