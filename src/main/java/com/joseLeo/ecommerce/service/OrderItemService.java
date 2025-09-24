package com.joseLeo.ecommerce.service;

import com.joseLeo.ecommerce.entity.OrderItem;
import com.joseLeo.ecommerce.repository.OrderItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderItemService {
    private final OrderItemRepository repository;

    public OrderItemService(OrderItemRepository repository) {
        this.repository = repository;
    }

    public List<OrderItem> getAllOrderItems() {
        return repository.findAll();
    }

    public OrderItem getOrderItemById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public OrderItem saveOrderItem(OrderItem orderItem) {
        return repository.save(orderItem);
    }

    public void deleteOrderItem(Long id) {
        repository.deleteById(id);
    }
}
