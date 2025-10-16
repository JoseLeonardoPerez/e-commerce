package com.joseLeo.ecommerce.service;

import com.joseLeo.ecommerce.entity.Role;
import com.joseLeo.ecommerce.entity.User;
import com.joseLeo.ecommerce.entity.Address;
import com.joseLeo.ecommerce.repository.RoleRepository;
import com.joseLeo.ecommerce.repository.UserRepository;
import com.joseLeo.ecommerce.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           AddressRepository addressRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ------------------- REGISTRO DE USUARIO -------------------
    @Override
    public User registerUser(User user, Address address) {
        if (existsByEmail(user.getEmail())) {
            throw new RuntimeException("El correo ya está registrado.");
        }

        // Encriptar contraseña
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Rol por defecto
        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Rol predeterminado no encontrado."));
        user.setRole(defaultRole);

        // Guardar usuario
        User savedUser = userRepository.save(user);

        // Guardar dirección vinculada al usuario
        address.setUser(savedUser);
        addressRepository.save(address);

        return savedUser;
    }

    @Override
    public User registerUser(User user) {
        return null;
    }

    // ------------------- AUTENTICACIÓN -------------------
    @Override
    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    // ------------------- CONSULTAS Y CRUD -------------------
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

                    if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                    }

                    if (updatedUser.getRole() != null) {
                        Role role = roleRepository.findByName(updatedUser.getRole().getName())
                                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
                        existingUser.setRole(role);
                    }

                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    // ------------------- UTILITARIOS -------------------
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void saveAddress(Address address) {
        addressRepository.save(address);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Address getAddressByUserId(Long userId) {
        List<Address> addresses = addressRepository.findByUserId(userId);
        if (addresses.isEmpty()) {
            throw new RuntimeException("Dirección no encontrada para el usuario con id: " + userId);
        }
        return addresses.get(0); // Tomamos la primera (única) dirección
    }

}
