package com.joseLeo.ecommerce.controller;

import com.joseLeo.ecommerce.entity.CartItem;
import com.joseLeo.ecommerce.service.CartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart-items")
public class CartItemController {

    private final CartItemService service;

    public CartItemController(CartItemService service) {
        this.service = service;
    }

    // Listar ítems de un carrito
    @GetMapping("/cart/{cartId}")
    public ResponseEntity<List<CartItem>> getByCartId(@PathVariable Long cartId) {
        return ResponseEntity.ok(service.getItemsByCartId(cartId));
    }

    // Agregar ítem al carrito
    @PostMapping
    public ResponseEntity<CartItem> addItem(@RequestBody CartItem item) {
        return ResponseEntity.ok(service.saveItem(item));
    }

    // Actualizar cantidad de un ítem
    @PutMapping("/{id}")
    public ResponseEntity<CartItem> updateItem(@PathVariable Long id, @RequestBody CartItem item) {
        item.setId(id);
        return ResponseEntity.ok(service.saveItem(item));
    }

    // Eliminar ítem
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        service.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
