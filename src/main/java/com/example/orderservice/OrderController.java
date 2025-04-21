package com.example.orderservice;

import com.example.orderservice.model.Order;
import com.example.orderservice.Service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public Order createOrder(@RequestBody Order order) throws JsonProcessingException {
        return orderService.createOrder(order);
    }
}
