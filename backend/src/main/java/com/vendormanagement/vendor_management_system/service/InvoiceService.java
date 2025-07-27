package com.vendormanagement.vendor_management_system.service;

import com.vendormanagement.vendor_management_system.dto.InvoiceRequestDto;
import com.vendormanagement.vendor_management_system.entity.Invoice;

import java.util.List;
import java.util.UUID;

public interface InvoiceService {
    Invoice createInvoice(InvoiceRequestDto dto);
    List<Invoice> getInvoicesByVendorService(UUID vendorServiceId);
    Invoice getInvoiceById(UUID id);

}