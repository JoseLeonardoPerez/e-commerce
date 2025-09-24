package com.joseLeo.ecommerce.controller;

import com.joseLeo.ecommerce.entity.OrderItem;
import com.joseLeo.ecommerce.service.OrderItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-items")
public class OrderItemController {

    private final OrderItemService service;

    public OrderItemController(OrderItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<OrderItem> getAll() {
        return service.getAllOrderItems();
    }

    @PostMapping
    public OrderItem save(@RequestBody OrderItem orderItem) {
        return service.saveOrderItem(orderItem);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteOrderItem(id);
    }
}