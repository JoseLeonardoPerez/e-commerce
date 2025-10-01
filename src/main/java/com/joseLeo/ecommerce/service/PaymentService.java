package com.joseLeo.ecommerce.service;

import com.joseLeo.ecommerce.entity.Order;
import com.joseLeo.ecommerce.entity.Payment;
import com.joseLeo.ecommerce.entity.PaymentMethod;
import com.joseLeo.ecommerce.entity.PaymentStatus;
import com.joseLeo.ecommerce.repository.OrderRepository;
import com.joseLeo.ecommerce.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    // Listar todos los pagos
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Obtener pago por ID
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    // Guardar pago (para pruebas o creación manual)
    public Payment savePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    // Borrar pago
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    // Actualizar solo el estado del pago
    public Payment updatePaymentStatus(Long id, PaymentStatus status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con id " + id));
        payment.setStatus(status);
        return paymentRepository.save(payment);
    }

    // Pagar una orden con método dinámico
    public Payment payOrder(Long orderId, PaymentMethod method, Double amount,
                            String cardNumber, String expiry, String cvv,
                            String paypalEmail, String mpToken) {

        // Buscar la orden
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con id " + orderId));

        // Crear o actualizar el Payment
        Payment payment = order.getPayment();
        if (payment == null) {
            payment = new Payment();
            payment.setOrder(order);
        }

        // Convertir enum a String para el campo method
        payment.setMethod(method.name());
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());

        // Marcar automáticamente como COMPLETED si es pago digital
        switch (method) {
            case CARD:
            case PAYPAL:
            case MERCADOPAGO:
                payment.setStatus(PaymentStatus.COMPLETED);
                break;
            case MANUAL:
                payment.setStatus(PaymentStatus.PENDING);
                break;
        }

        return paymentRepository.save(payment);
    }

}
