package com.joseLeo.ecommerce.service;

import com.joseLeo.ecommerce.entity.Role;
import com.joseLeo.ecommerce.entity.User;
import com.joseLeo.ecommerce.repository.RoleRepository;
import com.joseLeo.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(User user) {
        // Encriptar contraseña
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Asignar rol por defecto "ROLE_USER"
        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Rol predeterminado no encontrado"));
        user.setRole(defaultRole);

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(updatedUser.getName());
                    existingUser.setEmail(updatedUser.getEmail());

                    // 🔑 Encriptar nueva contraseña si se pasa
                    if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                    }

                    // 🔑 Actualizar rol si se pasa
                    if (updatedUser.getRole() != null) {
                        Role role = roleRepository.findByName(updatedUser.getRole().getName())
                                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
                        existingUser.setRole(role);
                    }

                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    @Override
    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
