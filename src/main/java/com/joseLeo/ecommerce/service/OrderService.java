package com.joseLeo.ecommerce.service;

import com.joseLeo.ecommerce.entity.*;
import com.joseLeo.ecommerce.repository.CartRepository;
import com.joseLeo.ecommerce.repository.OrderRepository;
import com.joseLeo.ecommerce.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final PaymentRepository paymentRepository;

    public OrderService(OrderRepository orderRepository,
                        CartRepository cartRepository,
                        CartService cartService,
                        PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.paymentRepository = paymentRepository;
    }

    // Obtener todas las órdenes
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Obtener una orden por ID
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    // Crear una orden desde el carrito del usuario
    public Order createOrderFromCart(Long userId) {
        Optional<Cart> optionalCart = cartService.getCartByUserId(userId);

        if (optionalCart.isEmpty()) {
            throw new RuntimeException("Carrito no encontrado para el usuario " + userId);
        }

        Cart cart = optionalCart.get();

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Carrito vacío");
        }

        // Crear la orden
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        // Copiar los items del carrito a la orden
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getProduct().getPrice()); // <--- set precio
                    orderItem.setOrder(order); // relación bidireccional
                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setItems(orderItems);

        // Guardar la orden
        Order savedOrder = orderRepository.save(order);

        // Crear el pago automáticamente
        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setAmount(savedOrder.calculateTotal());
        payment.setMethod("MANUAL"); // o cualquier método por defecto
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.PENDING); // enum
        paymentRepository.save(payment);

        savedOrder.setPayment(payment);
        orderRepository.save(savedOrder); // actualizar la orden con el pago

        // Vaciar el carrito
        cart.getItems().clear();
        cartRepository.save(cart);

        return savedOrder;
    }

    // Borrar una orden
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
