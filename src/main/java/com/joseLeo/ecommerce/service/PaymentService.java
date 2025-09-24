package com.joseLeo.ecommerce.service;

import com.joseLeo.ecommerce.entity.Payment;
import com.joseLeo.ecommerce.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository repository;

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    public List<Payment> getAllPayments() {
        return repository.findAll();
    }

    public Payment getPaymentById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Payment savePayment(Payment payment) {
        return repository.save(payment);
    }

    public void deletePayment(Long id) {
        repository.deleteById(id);
    }
}
