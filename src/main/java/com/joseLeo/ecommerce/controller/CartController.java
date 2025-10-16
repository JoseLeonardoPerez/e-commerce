package com.joseLeo.ecommerce.controller;

import com.joseLeo.ecommerce.entity.Cart;
import com.joseLeo.ecommerce.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Obtener todos los carritos
    @GetMapping
    public List<Cart> getAll() {
        return cartService.getAllCarts();
    }

    // Obtener carrito por usuario
    @GetMapping("/users/{userId}")
    public ResponseEntity<Cart> getCartByUserId(@PathVariable Long userId) {
        Optional<Cart> optionalCart = cartService.getCartByUserId(userId);
        if (optionalCart.isPresent()) {
            return ResponseEntity.ok(optionalCart.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear o actualizar carrito
    @PostMapping
    public Cart save(@RequestBody Cart cart) {
        return cartService.saveCart(cart);
    }

    // Eliminar carrito por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }

    // Eliminar carrito por usuario
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteByUserId(@PathVariable Long userId) {
        Optional<Cart> optionalCart = cartService.getCartByUserId(userId);
        if (optionalCart.isPresent()) {
            cartService.deleteCart(optionalCart.get().getId());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/users/{userId}/items")
    public ResponseEntity<?> addItemToCart(
            @PathVariable Long userId,
            @RequestBody AddToCartRequest request) {

        try {
            Cart updated = cartService.addProductToCart(userId, request.getProductId(), request.getQuantity());
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DTO interno para recibir datos del producto a agregar
    public static class AddToCartRequest {
        private Long productId;
        private Integer quantity;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }

}
