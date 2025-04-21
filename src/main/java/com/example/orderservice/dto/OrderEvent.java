package com.example.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OrderEvent {
    private UUID eventId;
    private String eventType;
    private Long orderId;
    private String product;
    private int quantity;
    private BigDecimal price;
    private String customerEmail;
    private LocalDateTime createdAt;
}