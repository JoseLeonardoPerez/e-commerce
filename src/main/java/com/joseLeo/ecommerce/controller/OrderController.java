package com.joseLeo.ecommerce.controller;

import com.joseLeo.ecommerce.entity.Order;
import com.joseLeo.ecommerce.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public List<Order> getAll() {
        return service.getAllOrders();
    }

    @PostMapping
    public Order save(@RequestBody Order order) {
        return service.saveOrder(order);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteOrder(id);
    }
}