package com.joseLeo.ecommerce.controller;

import com.joseLeo.ecommerce.dto.AuthRequest;
import com.joseLeo.ecommerce.dto.AuthResponse;
import com.joseLeo.ecommerce.dto.RegisterRequest;
import com.joseLeo.ecommerce.entity.Address;
import com.joseLeo.ecommerce.entity.Role;
import com.joseLeo.ecommerce.entity.User;
import com.joseLeo.ecommerce.repository.RoleRepository;
import com.joseLeo.ecommerce.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public AuthController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        User user = userService.authenticate(request.getEmail(), request.getPassword());
        if (user != null) {
            return ResponseEntity.ok(
                    new AuthResponse("Login exitoso para: " + user.getName() + " (Role: " + user.getRole().getName() + ")")
            );
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Email o contraseña incorrectos"));
        }
    }

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse("El email ya está registrado"));
        }

        // Crear usuario
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // se encripta dentro del servicio

        // Crear dirección
        Address address = new Address();
        address.setCity(request.getCity());
        address.setCountry(request.getCountry());
        address.setState(request.getState());
        address.setStreet(request.getStreet());
        address.setZip(request.getZip());

        // Registrar usuario y dirección con el nuevo método
        userService.registerUser(user, address);

        return ResponseEntity.ok(new AuthResponse("Registro exitoso para: " + user.getName()));
    }
}
