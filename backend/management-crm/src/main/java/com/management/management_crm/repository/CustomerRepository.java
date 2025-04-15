package com.management.management_crm.repository;

import java.time.OffsetDateTime;
import java.util.List;

/* import java.util.Optional; */

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.management.management_crm.models.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
        // Custom query to find customers based on user's restaurant
        @Query("SELECT c FROM CustomerEntity c WHERE c.restaurant.user.id = :userId")
        Page<CustomerEntity> findCustomersByUserId(Long userId, Pageable pageable);

        @Query("SELECT c FROM CustomerEntity c WHERE c.restaurant.id = :restaurantId " +
                        "AND LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
        Page<CustomerEntity> searchByRestaurantIdAndCustomerName(
                        @Param("restaurantId") Long restaurantId,
                        @Param("searchTerm") String searchTerm,
                        Pageable pageable);

        // Fallback method for all customers by restaurantId
        Page<CustomerEntity> findByRestaurantId(
                        @Param("restaurantId") Long restaurantId,
                        Pageable pageable);

        // Get all customers for a restaurant (no pagination, for analytics)
        List<CustomerEntity> findByRestaurantId(@Param("restaurantId") Long restaurantId);

        // Count all customers for a restaurant
        long countByRestaurantId(@Param("restaurantId") Long restaurantId);

        // Count customers created between two dates (e.g., this month, last month)
        long countByRestaurantIdAndCreatedAtBetween(
                        @Param("restaurantId") Long restaurantId,
                        @Param("start") OffsetDateTime start,
                        @Param("end") OffsetDateTime end);
}
