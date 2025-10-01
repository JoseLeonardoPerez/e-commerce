package com.joseLeo.ecommerce.service;

import com.joseLeo.ecommerce.entity.Invoice;
import com.joseLeo.ecommerce.entity.OrderItem;
import com.joseLeo.ecommerce.repository.InvoiceRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class InvoicePdfService {

    private final InvoiceRepository invoiceRepository;

    public InvoicePdfService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public byte[] generateInvoicePdf(Long invoiceId) {
        try {
            // Buscamos la factura
            Invoice invoice = invoiceRepository.findById(invoiceId)
                    .orElseThrow(() -> new RuntimeException("Invoice not found"));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, baos);

            document.open();

            // Número y fecha de la factura
            document.add(new Paragraph("Factura #" + invoice.getId()));
            document.add(new Paragraph("Fecha: " + invoice.getDate()));

            // Cliente a partir de la orden asociada
            String customerName = "Desconocido";
            if (invoice.getOrder() != null && invoice.getOrder().getUser() != null) {
                customerName = invoice.getOrder().getUser().getName();
            }
            document.add(new Paragraph("Cliente: " + customerName));

            // Total calculado sumando los ítems de la orden
            double totalAmount = 0.0;
            if (invoice.getOrder() != null && invoice.getOrder().getItems() != null) {
                for (OrderItem item : invoice.getOrder().getItems()) {
                    totalAmount += item.getPrice() * item.getQuantity();
                }
            }
            document.add(new Paragraph("Monto total: $" + totalAmount));

            document.add(new Paragraph("Gracias por su compra."));
            document.close();

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }
    }
}