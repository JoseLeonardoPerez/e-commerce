package com.joseLeo.ecommerce.config;

import com.joseLeo.ecommerce.entity.Role;
import com.joseLeo.ecommerce.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verificar si el rol "USER" ya existe
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
            System.out.println("Rol USER creado.");
        } else {
            System.out.println("Rol USER ya existe, no se crea.");
        }

        // No creamos rol ADMIN automáticamente
    }
}

