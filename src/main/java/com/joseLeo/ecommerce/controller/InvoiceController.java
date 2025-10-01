package com.joseLeo.ecommerce.controller;

import com.joseLeo.ecommerce.entity.Invoice;
import com.joseLeo.ecommerce.service.InvoicePdfService;
import com.joseLeo.ecommerce.service.InvoiceService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final InvoicePdfService pdfService;

    public InvoiceController(InvoiceService invoiceService, InvoicePdfService pdfService) {
        this.invoiceService = invoiceService;
        this.pdfService = pdfService;
    }

    // Listar todas las facturas
    @GetMapping
    public List<Invoice> getAll() {
        return invoiceService.getAllInvoices();
    }

    // Crear una nueva factura
    @PostMapping
    public Invoice save(@RequestBody Invoice invoice) {
        return invoiceService.saveInvoice(invoice);
    }

    // Borrar una factura
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
    }

    // Generar y descargar PDF de la factura
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getInvoicePdf(@PathVariable Long id) {
        try {
            byte[] pdfBytes = pdfService.generateInvoicePdf(id);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=factura_" + id + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}