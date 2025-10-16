package com.joseLeo.ecommerce.service;

import com.joseLeo.ecommerce.entity.User;
import com.joseLeo.ecommerce.entity.Address;
import java.util.List;
import java.util.Optional;

public interface UserService {

    // --- Registro y autenticación ---
    User registerUser(User user, Address address);
    User registerUser(User user); // ✅ Nueva sobrecarga
    User authenticate(String email, String password);

    // --- Consultas básicas ---
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);

    // --- CRUD de usuario ---
    User updateUser(Long id, User updatedUser);
    void deleteUser(Long id);

    // --- Utilitarios ---
    boolean existsByEmail(String email);
    String encodePassword(String rawPassword);
    void save(User user);
    void saveAddress(Address address);

    User findByEmail(String email);

    Address getAddressByUserId(Long userId);

}
