package com.joseLeo.ecommerce.controller;

import com.joseLeo.ecommerce.entity.Order;
import com.joseLeo.ecommerce.entity.User;
import com.joseLeo.ecommerce.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 🛒 Crear orden desde carrito (autenticado)
    @PostMapping
    public ResponseEntity<?> createOrder(Authentication authentication) {
        try {
            String email = authentication.getName();
            Order order = orderService.createOrderForUser(email);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 🛍 Crear orden directa (autenticado)
    @PostMapping("/direct")
    public ResponseEntity<?> createDirectOrder(
            @RequestParam Long productId,
            @RequestParam int quantity,
            Authentication authentication) {
        try {
            String email = authentication.getName();
            Order order = orderService.createDirectOrderForUser(email, productId, quantity);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 📦 Crear orden enviada desde el frontend (maneja ambas formas)
    @PostMapping("/frontend")
    public ResponseEntity<?> createOrderFromFrontend(
            @RequestBody Map<String, Object> orderData,
            Authentication authentication) {
        try {
            String email = authentication != null ? authentication.getName() : null;
            Order order = orderService.createOrderFromFrontend(orderData, email);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("❌ Error al crear la orden: " + e.getMessage());
        }
    }

    // 📋 Obtener todas las órdenes
    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(
            @RequestBody Map<String, Object> paymentData,
            Authentication authentication) {

        try {
            String email = authentication.getName();
            boolean paymentSuccess = Boolean.parseBoolean(paymentData.getOrDefault("success", "false").toString());

            if (!paymentSuccess) {
                return ResponseEntity.badRequest().body("❌ Error en el pago. El carrito se mantiene intacto.");
            }

            // Si el pago fue exitoso, limpiamos el carrito
            User user = orderService.getUserByEmail(email);
            orderService.clearUserCart(user.getId());

            return ResponseEntity.ok("✅ Pago realizado con éxito. Carrito vaciado correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("❌ Error al confirmar el pago: " + e.getMessage());
        }
    }
}
