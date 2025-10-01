package com.joseLeo.ecommerce.controllers;

import com.joseLeo.ecommerce.dto.AuthRequest;
import com.joseLeo.ecommerce.dto.AuthResponse;
import com.joseLeo.ecommerce.entity.User;
import com.joseLeo.ecommerce.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        User user = userService.authenticate(request.getEmail(), request.getPassword());
        if (user != null) {
            return ResponseEntity.ok(new AuthResponse("Login exitoso para: " + user.getName() + " (Role: " + user.getRole().getName() + ")"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Email o contraseña incorrectos"));
        }
    }
}
