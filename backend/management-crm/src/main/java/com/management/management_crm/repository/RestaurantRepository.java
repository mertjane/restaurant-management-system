package com.management.management_crm.repository;

/* import java.util.Optional; */
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.management.management_crm.models.RestaurantEntity;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    // Custom query to find restaurants by user ID
    @Query("SELECT r FROM RestaurantEntity r WHERE r.user.id = :userId")
    List<RestaurantEntity> findByUserId(@Param("userId") Long userId);
}
