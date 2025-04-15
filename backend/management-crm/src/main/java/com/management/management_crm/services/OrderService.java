package com.management.management_crm.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.management.management_crm.dto.OrderDTO;
import com.management.management_crm.models.OrderEntity;
import com.management.management_crm.repository.OrderRepository;

@Service
public class OrderService {

  @Autowired
  private OrderRepository orderRepository;

  public List<OrderDTO> getOrdersByRestaurantId(Long restaurantId) {
    return orderRepository.findByRestaurantId(restaurantId)
        .stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  private OrderDTO convertToDTO(OrderEntity entity) {
    OrderDTO dto = new OrderDTO();
    dto.setId(entity.getId());
    dto.setRestaurantId(entity.getRestaurantId());
    dto.setOrderDetails(entity.getOrderDetails());
    dto.setStatus(entity.getStatus());
    dto.setCreatedAt(entity.getCreatedAt());
    dto.setUpdatedAt(entity.getUpdatedAt());
    return dto;
  }
}
