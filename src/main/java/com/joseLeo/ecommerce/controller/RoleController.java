package com.joseLeo.ecommerce.controller;

import com.joseLeo.ecommerce.entity.Role;
import com.joseLeo.ecommerce.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }

    @GetMapping
    public List<Role> getAll() {
        return service.getAllRoles();
    }

    @PostMapping
    public Role save(@RequestBody Role role) {
        return service.saveRole(role);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteRole(id);
    }
}
