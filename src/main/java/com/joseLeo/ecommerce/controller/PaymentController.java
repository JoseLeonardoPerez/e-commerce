package com.joseLeo.ecommerce.controller;

import com.joseLeo.ecommerce.entity.Payment;
import com.joseLeo.ecommerce.entity.PaymentStatus;
import com.joseLeo.ecommerce.entity.PaymentMethod;
import com.joseLeo.ecommerce.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    // Listar todos los pagos
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(service.getAllPayments());
    }

    // Obtener pago por ID
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        Payment payment = service.getPaymentById(id);
        if (payment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(payment);
    }

    // Crear pago (solo para pruebas, en tu flujo real se genera automáticamente)
    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        Payment saved = service.savePayment(payment);
        return ResponseEntity.ok(saved);
    }

    // Borrar pago
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        service.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    // Actualizar solo el estado del pago
    @PutMapping("/{id}/status")
    public ResponseEntity<Payment> updatePaymentStatus(@PathVariable Long id, @RequestParam PaymentStatus status) {
        try {
            Payment updated = service.updatePaymentStatus(id, status);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // --- NUEVO: Pagar una orden con método dinámico ---
    @PutMapping("/orders/{orderId}/pay")
    public ResponseEntity<Payment> payOrder(
            @PathVariable Long orderId,
            @RequestParam PaymentMethod method,
            @RequestParam Double amount,
            @RequestParam(required = false) String cardNumber,
            @RequestParam(required = false) String expiry,
            @RequestParam(required = false) String cvv,
            @RequestParam(required = false) String paypalEmail,
            @RequestParam(required = false) String mpToken
    ) {
        try {
            Payment payment = service.payOrder(orderId, method, amount, cardNumber, expiry, cvv, paypalEmail, mpToken);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
