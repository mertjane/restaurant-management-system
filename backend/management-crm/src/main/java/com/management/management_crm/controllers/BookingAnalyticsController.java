package com.management.management_crm.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.management.management_crm.dto.BookingAnalyticsDTO;
import com.management.management_crm.services.BookingAnalyticsService;

@RestController
@RequestMapping("/analytics")
public class BookingAnalyticsController {
  private final BookingAnalyticsService service;

  public BookingAnalyticsController(BookingAnalyticsService service) {
    this.service = service;
  }

  @PostMapping("/bookings/generate")
  public ResponseEntity<BookingAnalyticsDTO> generateDailyAnalytics(
      @RequestParam Long restaurantId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    BookingAnalyticsDTO dto = service.computeAndSaveAnalytics(restaurantId, date);
    return ResponseEntity.ok(dto);
  }

  @GetMapping
  public ResponseEntity<List<BookingAnalyticsDTO>> getAnalytics(
      @RequestParam Long restaurantId,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    return ResponseEntity.ok(service.getAnalyticsBetween(restaurantId, startDate, endDate));
  }

}
