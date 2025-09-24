package com.joseLeo.ecommerce.service;

import com.joseLeo.ecommerce.entity.Role;
import com.joseLeo.ecommerce.repository.RoleRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoleService {
    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    public List<Role> getAllRoles() {
        return repository.findAll();
    }

    public Role getRoleById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Role saveRole(Role role) {
        return repository.save(role);
    }

    public void deleteRole(Long id) {
        repository.deleteById(id);
    }
}
