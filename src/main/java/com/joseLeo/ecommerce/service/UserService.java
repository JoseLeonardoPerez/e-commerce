package com.joseLeo.ecommerce.service;

import com.joseLeo.ecommerce.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    void deleteUser(Long id);

    User updateUser(Long id, User user); // 👈 nuevo

    // Método extra para login temporal
    User authenticate(String email, String password);
}

