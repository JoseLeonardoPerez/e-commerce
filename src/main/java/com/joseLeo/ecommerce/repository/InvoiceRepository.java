package com.joseLeo.ecommerce.repository;

import com.joseLeo.ecommerce.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
