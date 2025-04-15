package com.management.management_crm.controllers;

import java.time.YearMonth;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.management.management_crm.dto.CustomerAnalyticsDTO;
import com.management.management_crm.models.CustomerAnalyticsEntity;
import com.management.management_crm.services.CustomerAnalyticsService;

@RestController
@RequestMapping("/api/v1/analytics-customers")
public class CustomerAnalyticsController {

  @Autowired
  private CustomerAnalyticsService customerAnalyticsService;

  // POST endpoint to calculate analytics
  @PostMapping("/calculate")
  public ResponseEntity<String> calculateAnalytics(@RequestParam Long restaurantId,
      @RequestParam YearMonth currentMonth) {
    // Call the service method to calculate analytics
    customerAnalyticsService.calculateCustomerAnalytics(restaurantId, currentMonth);

    // Return a success message
    return ResponseEntity.ok("Customer analytics calculation triggered successfully!");
  }

  // GET endpoint to retrieve analytics for a specific restaurant and month
  @GetMapping("/get")
  public ResponseEntity<CustomerAnalyticsDTO> getCustomerAnalytics(@RequestParam Long restaurantId,
      @RequestParam YearMonth currentMonth) {
    // Fetch the analytics from the database
    Optional<CustomerAnalyticsEntity> analyticsOpt = customerAnalyticsService.getCustomerAnalytics(restaurantId,
        currentMonth);

    if (analyticsOpt.isPresent()) {
      CustomerAnalyticsDTO dto = convertToDTO(analyticsOpt.get());
      return ResponseEntity.ok(dto);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  // Convert Entity to DTO
  private CustomerAnalyticsDTO convertToDTO(CustomerAnalyticsEntity entity) {
    return new CustomerAnalyticsDTO(
        entity.getId(),
        entity.getRestaurantId(),
        entity.getDate(),
        entity.getTotalCusts(),
        entity.getNewCusts(),
        entity.getTotalCustsChange(),
        entity.getNewCustsChange(),
        entity.getPeakDay());
  }
}
