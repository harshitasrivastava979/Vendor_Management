package com.vendormanagement.vendor_management_system.controller;

import com.vendormanagement.vendor_management_system.dto.InvoiceRequestDto;
import com.vendormanagement.vendor_management_system.entity.Invoice;
import com.vendormanagement.vendor_management_system.mapper.InvoiceMap;
import com.vendormanagement.vendor_management_system.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceMap invoiceMap;

    @Autowired
    public InvoiceController(InvoiceService invoiceService, InvoiceMap invoiceMap) {
        this.invoiceService = invoiceService;
        this.invoiceMap = invoiceMap;
    }

    @GetMapping("/by-vendor-service/{vendorServiceId}")
    public ResponseEntity<List<Invoice>> getInvoicesByVendorService(@PathVariable UUID vendorServiceId) {
        List<Invoice> invoices = invoiceService.getInvoicesByVendorService(vendorServiceId);
        return ResponseEntity.ok(invoices);
    }

    @PostMapping
    public ResponseEntity<InvoiceRequestDto> createInvoice(@RequestBody InvoiceRequestDto dto) {
        Invoice savedInvoice = invoiceService.createInvoice(dto);
        InvoiceRequestDto responseDto = invoiceMap.mapInvoiceToDto(savedInvoice);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}


