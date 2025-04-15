package com.management.management_crm.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class OrderDTO {
    private Long id;
    private Long restaurantId;
    private Map<String, Object> orderDetails;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}