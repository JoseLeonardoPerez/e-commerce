package com.joseLeo.ecommerce.service;

import com.joseLeo.ecommerce.entity.Cart;
import com.joseLeo.ecommerce.entity.CartItem;
import com.joseLeo.ecommerce.entity.Product;
import com.joseLeo.ecommerce.entity.User;
import com.joseLeo.ecommerce.repository.CartRepository;
import com.joseLeo.ecommerce.repository.ProductRepository;
import com.joseLeo.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository,
                       UserRepository userRepository,
                       ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // Obtener todos los carritos
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    // Obtener carrito por id de usuario
    public Optional<Cart> getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    // Crear o actualizar carrito
    public Cart saveCart(Cart cart) {
        // 🔑 Buscar usuario completo en DB
        if (cart.getUser() != null && cart.getUser().getId() != null) {
            User user = userRepository.findById(cart.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id " + cart.getUser().getId()));
            cart.setUser(user);
        }

        // 🔑 Buscar productos completos en DB
        if (cart.getItems() != null) {
            for (CartItem item : cart.getItems()) {
                if (item.getProduct() != null && item.getProduct().getId() != null) {
                    Product product = productRepository.findById(item.getProduct().getId())
                            .orElseThrow(() -> new RuntimeException("Producto no encontrado con id " + item.getProduct().getId()));
                    item.setProduct(product);
                }
                item.setCart(cart); // relación bidireccional
            }
        }

        return cartRepository.save(cart);
    }

    // Eliminar carrito por id
    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }

    // Eliminar carrito por id de usuario
    public void deleteCartByUserId(Long userId) {
        cartRepository.findByUserId(userId).ifPresent(cartRepository::delete);
    }
}
