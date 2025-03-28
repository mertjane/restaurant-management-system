package com.management.management_crm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.management.management_crm.models.MenuEntity;

public interface MenuRepository extends JpaRepository<MenuEntity, Long> {
    @Query("SELECT m FROM MenuEntity m WHERE m.restaurant.id = :restaurantId")
    Page<MenuEntity> findMenuByRestaurantId(@Param("restaurantId") Long restaurantId, Pageable pageable);
}
