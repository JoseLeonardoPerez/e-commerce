package com.joseLeo.ecommerce.controller;

import com.joseLeo.ecommerce.entity.Payment;
import com.joseLeo.ecommerce.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Payment> getAll() {
        return service.getAllPayments();
    }

    @PostMapping
    public Payment save(@RequestBody Payment payment) {
        return service.savePayment(payment);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deletePayment(id);
    }
}
