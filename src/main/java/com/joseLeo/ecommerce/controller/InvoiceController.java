package com.joseLeo.ecommerce.controller;

import com.joseLeo.ecommerce.entity.Invoice;
import com.joseLeo.ecommerce.service.InvoiceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService service;

    public InvoiceController(InvoiceService service) {
        this.service = service;
    }

    @GetMapping
    public List<Invoice> getAll() {
        return service.getAllInvoices();
    }

    @PostMapping
    public Invoice save(@RequestBody Invoice invoice) {
        return service.saveInvoice(invoice);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteInvoice(id);
    }
}