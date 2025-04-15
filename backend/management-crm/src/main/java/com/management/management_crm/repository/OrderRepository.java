package com.management.management_crm.repository;

import com.management.management_crm.models.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
  List<OrderEntity> findByRestaurantId(Long restaurantId);
}
