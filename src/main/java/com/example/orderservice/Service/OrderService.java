package com.example.orderservice.Service;
import com.example.orderservice.dto.OrderEvent;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OutboxEvent;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public Order createOrder(Order order) throws JsonProcessingException {
        Order savedOrder = orderRepository.save(order);

        // Create event
        OrderEvent event = new OrderEvent();
        event.setEventId(UUID.randomUUID());
        event.setEventType("ORDER_CREATED");
        event.setOrderId(savedOrder.getId());
        event.setProduct(savedOrder.getProduct());
        event.setQuantity(savedOrder.getQuantity());
        event.setPrice(savedOrder.getPrice());
        event.setCustomerEmail(savedOrder.getCustomerEmail());
        event.setCreatedAt(savedOrder.getCreatedAt());

        // Save to outbox
        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setEventType("ORDER_CREATED");
        outboxEvent.setAggregateType("Order");
        outboxEvent.setAggregateId(savedOrder.getId().toString());
        outboxEvent.setPayload(objectMapper.writeValueAsString(event));

        outboxEventRepository.save(outboxEvent);

        return savedOrder;
    }
}