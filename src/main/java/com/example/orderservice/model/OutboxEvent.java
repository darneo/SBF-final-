package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "outbox_events")
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID eventId = UUID.randomUUID();
    private String eventType;
    private String aggregateType;
    private String aggregateId;
    private String payload;
    private LocalDateTime createdAt = LocalDateTime.now();
    private boolean published = false;
}