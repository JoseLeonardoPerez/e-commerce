package com.joseLeo.ecommerce.repository;

import com.joseLeo.ecommerce.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Buscar carrito por id de usuario
    Optional<Cart> findByUserId(Long userId);
}
