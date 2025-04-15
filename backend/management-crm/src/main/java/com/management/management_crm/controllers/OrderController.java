package com.management.management_crm.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.management.management_crm.dto.OrderDTO;
import com.management.management_crm.services.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

  @Autowired
  private OrderService orderService;

  @GetMapping("/restaurant/{restaurantId}")
  public List<OrderDTO> getOrdersByRestaurantId(@PathVariable Long restaurantId) {
    return orderService.getOrdersByRestaurantId(restaurantId);
  }
}
