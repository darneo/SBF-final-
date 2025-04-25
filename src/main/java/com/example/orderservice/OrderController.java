package com.example.orderservice;

import com.example.orderservice.model.Order;
import com.example.orderservice.Service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    // Явный конструктор для внедрения зависимости
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) throws JsonProcessingException {
        return orderService.createOrder(order);
    }
}
