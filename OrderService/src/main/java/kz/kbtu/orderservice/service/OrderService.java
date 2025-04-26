package kz.kbtu.orderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.kbtu.orderservice.dto.OrderEvent;
import kz.kbtu.orderservice.model.Order;
import kz.kbtu.orderservice.model.OutboxEvent;
import kz.kbtu.orderservice.repository.OrderRepository;
import kz.kbtu.orderservice.repository.OutboxEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public OrderService(OrderRepository orderRepository,
                        OutboxEventRepository outboxEventRepository,
                        ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public Order createOrder(Order order) throws JsonProcessingException {
        Order savedOrder = orderRepository.save(order);
        saveOutboxEvent(savedOrder, "ORDER_CREATED");
        return savedOrder;
    }

    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Order getOrderById(long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order with ID " + id + " not found"));
    }

    @Transactional
    public Order updateOrder(long id, Order updatedOrder) {
        Order existing = getOrderById(id);
        existing.setProduct(updatedOrder.getProduct());
        existing.setQuantity(updatedOrder.getQuantity());
        existing.setPrice(updatedOrder.getPrice());
        existing.setCustomerEmail(updatedOrder.getCustomerEmail());

        Order saved = orderRepository.save(existing);

        try {
            saveOutboxEvent(saved, "ORDER_UPDATED");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize ORDER_UPDATED event", e);
        }

        return saved;
    }

    @Transactional
    public void deleteOrder(long id) {
        Order order = getOrderById(id);
        orderRepository.deleteById(id);

        try {
            saveOutboxEvent(order, "ORDER_DELETED");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize ORDER_DELETED event", e);
        }
    }

    private void saveOutboxEvent(Order order, String eventType) throws JsonProcessingException {
        OrderEvent event = new OrderEvent();
        event.setEventId(UUID.randomUUID());
        event.setEventType(eventType);
        event.setOrderId(order.getId());
        event.setProduct(order.getProduct());
        event.setQuantity(order.getQuantity());
        event.setPrice(order.getPrice());
        event.setCustomerEmail(order.getCustomerEmail());
        event.setCreatedAt(order.getCreatedAt());

        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setEventType(eventType);
        outboxEvent.setAggregateType("Order");
        outboxEvent.setAggregateId(order.getId().toString());
        outboxEvent.setPayload(objectMapper.writeValueAsString(event));

        outboxEventRepository.save(outboxEvent);
    }
}
