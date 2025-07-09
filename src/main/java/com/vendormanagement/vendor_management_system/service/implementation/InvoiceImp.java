package com.vendormanagement.vendor_management_system.service.implementation;

import com.vendormanagement.vendor_management_system.dto.InvoiceRequestDto;
import com.vendormanagement.vendor_management_system.entity.Invoice;
import com.vendormanagement.vendor_management_system.entity.VendorService;
import com.vendormanagement.vendor_management_system.mapper.InvoiceMap;
import com.vendormanagement.vendor_management_system.repository.InvoiceRepository;
import com.vendormanagement.vendor_management_system.repository.VendorServiceRepository;
import com.vendormanagement.vendor_management_system.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class InvoiceImp implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private VendorServiceRepository vendorServiceRepository;

    @Autowired
    private InvoiceMap invoiceMap;

    @Override
    public Invoice createInvoice(InvoiceRequestDto dto) {
        if (dto.getAmountExclusive() == null || dto.getVendorServiceId() == null) {
            throw new RuntimeException("Amount and Vendor Service ID are required.");
        }

        calculateInvoiceAmounts(dto);

        if (invoiceRepository.existsByInvoiceNumber(dto.getInvoiceNumber())) {
            throw new RuntimeException("Invoice number already exists.");
        }

        // Fetch VendorService using ID from DTO
        VendorService vendorService = vendorServiceRepository.findById(dto.getVendorServiceId())
                .orElseThrow(() -> new RuntimeException("VendorService not found"));

        // Pass vendorService instance to mapper and save invoice
        return invoiceRepository.save(invoiceMap.mapDtoToInvoice(dto, vendorService));
    }

    @Override
    public List<Invoice> getInvoicesByVendorService(UUID vendorServiceId) {
        return invoiceRepository.findByVendorServiceId(vendorServiceId);
    }

    private void calculateInvoiceAmounts(InvoiceRequestDto dto) {
        BigDecimal amountExclusive = dto.getAmountExclusive();
        BigDecimal gstPercent = dto.getGstPercent() != null ? dto.getGstPercent() : BigDecimal.ZERO;

        BigDecimal gstAmount = amountExclusive.multiply(gstPercent).divide(BigDecimal.valueOf(100));
        dto.setGstAmount(gstAmount);
        dto.setAmountInclusive(amountExclusive.add(gstAmount));

        if (dto.getTdsPercent() == null) {
            VendorService vendorService = vendorServiceRepository.findById(dto.getVendorServiceId())
                    .orElseThrow(() -> new RuntimeException("VendorService not found"));
            dto.setTdsPercent(vendorService.getTdsRate());
        }

        BigDecimal tdsAmount = amountExclusive.multiply(dto.getTdsPercent()).divide(BigDecimal.valueOf(100));
        dto.setTdsAmount(tdsAmount);
        BigDecimal finalPayable = amountExclusive.subtract(tdsAmount).add(gstAmount);

        dto.setFinalAmount(finalPayable);
    }

    @Override
    public Invoice getInvoiceById(UUID id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
    }
}
