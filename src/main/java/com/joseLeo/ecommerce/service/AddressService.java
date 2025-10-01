package com.joseLeo.ecommerce.service;

import com.joseLeo.ecommerce.entity.Address;
import com.joseLeo.ecommerce.repository.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    private final AddressRepository repository;

    public AddressService(AddressRepository repository) {
        this.repository = repository;
    }

    public List<Address> getAllAddresses() {
        return repository.findAll();
    }

    public Address getAddressById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Address saveAddress(Address address) {
        return repository.save(address);
    }

    public void deleteAddress(Long id) {
        repository.deleteById(id);
    }

    public List<Address> getAddressesByUserId(Long userId) {
        return repository.findByUserId(userId);
    }
}
