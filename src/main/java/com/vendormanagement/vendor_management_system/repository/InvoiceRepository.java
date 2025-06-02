package com.vendormanagement.vendor_management_system.repository;

import com.vendormanagement.vendor_management_system.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    boolean existsByInvoiceNumber(String invoiceNumber);

    List<Invoice> findByVendorServiceId(UUID vendorServiceId);
}