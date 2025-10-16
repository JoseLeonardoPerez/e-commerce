package com.joseLeo.ecommerce.service;

import com.joseLeo.ecommerce.entity.*;
import com.joseLeo.ecommerce.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        CartRepository cartRepository,
                        ProductRepository productRepository,
                        OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    /** ✅ Crea una orden con todos los productos del carrito */
    @Transactional
    public Order createOrderForUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Cart existingCart = cartRepository.findByUserId(user.getId()).orElse(null);
        if (existingCart == null || existingCart.getItems() == null || existingCart.getItems().isEmpty()) {
            throw new RuntimeException("No hay productos en el carrito");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0.0;

        for (CartItem cartItem : existingCart.getItems()) {
            Product product = cartItem.getProduct();
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
            total += product.getPrice() * cartItem.getQuantity();
        }

        order.setItems(orderItems);
        assignTotal(order, total);

        Order savedOrder = orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        // 🧹 NO limpiar carrito aquí.
        // Se vaciará solo cuando el pago esté confirmado exitosamente.


        return savedOrder;
    }

    /** ✅ Crea una orden directa desde un producto individual */
    @Transactional
    public Order createDirectOrderForUser(String email, Long productId, int quantity) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(product.getPrice());
        orderItem.setOrder(order);

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        order.setItems(orderItems);

        double total = product.getPrice() * quantity;
        assignTotal(order, total);

        Order savedOrder = orderRepository.save(order);
        orderItemRepository.save(orderItem);

        return savedOrder;
    }

    /** ✅ Método unificado que maneja orden directa o de carrito */
    @Transactional
    public Order createOrderFromFrontend(Map<String, Object> orderData, String email) {
        if (orderData == null || orderData.isEmpty()) {
            throw new RuntimeException("Datos de orden vacíos");
        }

        Long productId = orderData.get("productId") != null
                ? Long.parseLong(orderData.get("productId").toString())
                : null;

        Integer quantity = orderData.get("quantity") != null
                ? Integer.parseInt(orderData.get("quantity").toString())
                : null;

        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Usuario no autenticado");
        }

        // 🟢 Si hay productId → compra directa
        if (productId != null && quantity != null && quantity > 0) {
            return createDirectOrderForUser(email, productId, quantity);
        }

        // 🟣 Si no, compra desde carrito
        return createOrderForUser(email);
    }

    /** ✅ Listar todas las órdenes */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /** 🔧 Asignar el total independientemente del nombre del atributo */
    private void assignTotal(Order order, double total) {
        try {
            order.getClass().getMethod("setTotalAmount", double.class).invoke(order, total);
        } catch (NoSuchMethodException e) {
            try {
                order.getClass().getMethod("setTotal", double.class).invoke(order, total);
            } catch (Exception ex) {
                // sin campo total, se ignora
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al asignar total de la orden");
        }
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new RuntimeException("Usuario no encontrado con email: " + email);
        return user;
    }

    public void clearUserCart(Long userId) {
        cartRepository.findByUserId(userId).ifPresent(cart -> {
            cart.getItems().clear();
            cartRepository.save(cart);
        });
    }


}
