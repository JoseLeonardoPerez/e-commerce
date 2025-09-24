package com.joseLeo.ecommerce.service;

import com.joseLeo.ecommerce.entity.User;
import com.joseLeo.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User registerUser(User user) {
        // Más adelante podrías encriptar la contraseña
        return repository.save(user);
    }
}
