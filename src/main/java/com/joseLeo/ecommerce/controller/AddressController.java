package com.joseLeo.ecommerce.controller;

import com.joseLeo.ecommerce.entity.Address;
import com.joseLeo.ecommerce.service.AddressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
    }

    @GetMapping
    public List<Address> getAll() {
        return service.getAllAddresses();
    }

    @PostMapping
    public Address save(@RequestBody Address address) {
        return service.saveAddress(address);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteAddress(id);
    }
}