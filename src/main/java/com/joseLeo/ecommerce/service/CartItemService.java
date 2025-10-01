package com.joseLeo.ecommerce.service;

import com.joseLeo.ecommerce.entity.CartItem;
import com.joseLeo.ecommerce.repository.CartItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {

    private final CartItemRepository repository;

    public CartItemService(CartItemRepository repository) {
        this.repository = repository;
    }

    public List<CartItem> getItemsByCartId(Long cartId) {
        return repository.findByCartId(cartId);
    }

    public CartItem getItemById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public CartItem saveItem(CartItem item) {
        if (item.getId() != null) {
            // Es un UPDATE → primero cargo el existente
            CartItem existing = repository.findById(item.getId())
                    .orElseThrow(() -> new RuntimeException("Item no encontrado con id " + item.getId()));

            // Actualizo solo lo que quiero permitir
            existing.setQuantity(item.getQuantity());

            if (item.getCart() != null) {
                existing.setCart(item.getCart());
            }

            if (item.getProduct() != null) {
                existing.setProduct(item.getProduct());
            }

            return repository.save(existing);
        }

        // Es un CREATE → guardo tal cual
        return repository.save(item);
    }

    public void deleteItem(Long id) {
        repository.deleteById(id);
    }
}
