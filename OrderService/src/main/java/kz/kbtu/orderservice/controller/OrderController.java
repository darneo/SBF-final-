package kz.kbtu.orderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import kz.kbtu.orderservice.model.Order;
import kz.kbtu.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Создание нового заказа")
    @ApiResponse(responseCode = "200", description = "Заказ успешно создан")
    @PostMapping
    public Order createOrder(@RequestBody Order order) throws JsonProcessingException {
        return orderService.createOrder(order);
    }

    @Operation(summary = "Получить все заказы")
    @ApiResponse(responseCode = "200", description = "Список заказов")
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @Operation(summary = "Получить заказ по ID")
    @ApiResponse(responseCode = "200", description = "Найденный заказ")
    @ApiResponse(responseCode = "404", description = "Заказ не найден")
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable long id) {
        return orderService.getOrderById(id);
    }

    @Operation(summary = "Обновить заказ по ID")
    @ApiResponse(responseCode = "200", description = "Заказ успешно обновлён")
    @ApiResponse(responseCode = "404", description = "Заказ не найден")
    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable long id, @RequestBody Order order) {
        return orderService.updateOrder(id, order);
    }

    @Operation(summary = "Удалить заказ по ID")
    @ApiResponse(responseCode = "204", description = "Заказ успешно удалён")
    @ApiResponse(responseCode = "404", description = "Заказ не найден")
    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable long id) {
        orderService.deleteOrder(id);
    }
}
