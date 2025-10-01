package com.joseLeo.ecommerce.service;

import com.joseLeo.ecommerce.entity.Invoice;
import com.joseLeo.ecommerce.entity.Order;
import com.joseLeo.ecommerce.entity.User;
import com.joseLeo.ecommerce.repository.InvoiceRepository;
import com.joseLeo.ecommerce.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvoiceService {

    private final InvoiceRepository repository;
    private final OrderRepository orderRepository;

    public InvoiceService(InvoiceRepository repository, OrderRepository orderRepository) {
        this.repository = repository;
        this.orderRepository = orderRepository;
    }

    public List<Invoice> getAllInvoices() {
        return repository.findAll();
    }

    public Invoice getInvoiceById(Long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Crea una factura automáticamente a partir de una orden existente.
     * Se genera un número de factura simple y se llena la información de la orden.
     *
     * @param invoice Invoice que debe contener solo el ID de la orden en invoice.getOrder().getId()
     * @return Invoice creada con todos los datos de la orden
     */
    public Invoice saveInvoice(Invoice invoice) {
        if (invoice.getOrder() == null || invoice.getOrder().getId() == null) {
            throw new RuntimeException("Debe proporcionar el ID de la orden para generar la factura");
        }

        Long orderId = invoice.getOrder().getId();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con id " + orderId));

        Invoice newInvoice = new Invoice();
        newInvoice.setOrder(order);
        newInvoice.setCustomer(order.getUser()); // Asumiendo que Order tiene getUser()
        newInvoice.setTotal(order.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum());
        newInvoice.setDate(LocalDateTime.now());
        newInvoice.setInvoiceNumber("INV-" + System.currentTimeMillis());

        return repository.save(newInvoice);
    }

    public void deleteInvoice(Long id) {
        repository.deleteById(id);
    }
}
