package com.joseLeo.ecommerce.service;

import com.joseLeo.ecommerce.entity.Invoice;
import com.joseLeo.ecommerce.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InvoiceService {
    private final InvoiceRepository repository;

    public InvoiceService(InvoiceRepository repository) {
        this.repository = repository;
    }

    public List<Invoice> getAllInvoices() {
        return repository.findAll();
    }

    public Invoice getInvoiceById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Invoice saveInvoice(Invoice invoice) {
        return repository.save(invoice);
    }

    public void deleteInvoice(Long id) {
        repository.deleteById(id);
    }
}
