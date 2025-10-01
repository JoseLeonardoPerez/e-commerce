package com.joseLeo.ecommerce.config;

import com.joseLeo.ecommerce.entity.Role;
import com.joseLeo.ecommerce.entity.User;
import com.joseLeo.ecommerce.repository.RoleRepository;
import com.joseLeo.ecommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DevAdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DevAdminInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verificar si existe el rol ADMIN, si no, crearlo
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElse(null);
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }

        // Verificar si existe el rol MANAGER, si no, crearlo
        Role managerRole = roleRepository.findByName("ROLE_MANAGER").orElseGet(() -> {
            Role newRole = new Role();
            newRole.setName("ROLE_MANAGER");
            return roleRepository.save(newRole);
        });

        // Verificar si existe el usuario admin, si no, crearlo
        User adminUser = userRepository.findByEmail("admin@example.com");
        if (adminUser == null) {
            adminUser = new User();
            adminUser.setName("Admin");
            adminUser.setEmail("admin@example.com");
            // ✅ Guardar encriptado
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setRole(adminRole);
            userRepository.save(adminUser);
        }

        // Crear usuario manager si no existe
        User managerUser = userRepository.findByEmail("manager@example.com");
        if (managerUser == null) {
            managerUser = new User();
            managerUser.setName("Manager");
            managerUser.setEmail("manager@example.com");
            managerUser.setPassword(passwordEncoder.encode("manager123"));
            managerUser.setRole(managerRole);
            userRepository.save(managerUser);
        }
    }
}
