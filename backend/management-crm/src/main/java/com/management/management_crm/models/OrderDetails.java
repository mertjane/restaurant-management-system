package com.management.management_crm.models;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDetails implements Serializable {
  private List<String> cold_starters;
  private List<String> hot_starters;
  private List<String> mains;
  private List<String> sides;
  private List<String> drinks;
  private Double total_price;
}
