package com.example.orderservice.dto;

import java.math.BigDecimal;

public record OrderDTO(Long id, String orderNumber, String email, String skuCode, String name,
                       BigDecimal price, Integer quantity, UserDetails userDetails) {

  public record UserDetails(String email, String firstName, String lastName) {

  }

}
