package com.joseLeo.ecommerce.service;

import com.joseLeo.ecommerce.entity.Cart;
import com.joseLeo.ecommerce.repository.CartRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CartService {
    private final CartRepository repository;

    public CartService(CartRepository repository) {
        this.repository = repository;
    }

    public List<Cart> getAllCarts() {
        return repository.findAll();
    }

    public Cart getCartById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Cart saveCart(Cart cart) {
        return repository.save(cart);
    }

    public void deleteCart(Long id) {
        repository.deleteById(id);
    }
}

