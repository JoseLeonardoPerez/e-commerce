package com.joseLeo.ecommerce.controller;

import com.joseLeo.ecommerce.entity.Cart;
import com.joseLeo.ecommerce.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @GetMapping
    public List<Cart> getAll() {
        return service.getAllCarts();
    }

    @PostMapping
    public Cart save(@RequestBody Cart cart) {
        return service.saveCart(cart);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteCart(id);
    }
}