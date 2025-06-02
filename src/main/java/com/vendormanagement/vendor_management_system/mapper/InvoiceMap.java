package com.vendormanagement.vendor_management_system.mapper;

import com.vendormanagement.vendor_management_system.dto.InvoiceRequestDto;
import com.vendormanagement.vendor_management_system.entity.Invoice;
import com.vendormanagement.vendor_management_system.entity.VendorService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class InvoiceMap {

    public Invoice mapDtoToInvoice(InvoiceRequestDto dto, VendorService vendorService) {
        Invoice invoice = new Invoice();

        invoice.setVendorService(vendorService);
        invoice.setInvoiceMonth(dto.getMonth());

        // Convert String invoiceType in DTO to InvoiceType enum
        if (dto.getInvoiceType() != null) {
            invoice.setInvoiceType(Invoice.InvoiceType.valueOf(dto.getInvoiceType()));
        }

        invoice.setInvoiceNumber(dto.getInvoiceNumber());
        invoice.setGstPercent(dto.getGstPercent());
        invoice.setGstAmount(dto.getGstAmount());
        invoice.setAmountInclusive(dto.getAmountInclusive());
        invoice.setAmountExclusive(dto.getAmountExclusive());
        invoice.setTdsPercent(dto.getTdsPercent());
        invoice.setTdsAmount(dto.getTdsAmount());
        invoice.setFinalAmount(dto.getFinalAmount());

        return invoice;
    }

    public InvoiceRequestDto mapInvoiceToDto(Invoice invoice) {
        InvoiceRequestDto dto = new InvoiceRequestDto();

        dto.setVendorServiceId(invoice.getVendorService().getId());
        dto.setMonth(invoice.getInvoiceMonth());

        // Convert InvoiceType enum to String
        if (invoice.getInvoiceType() != null) {
            dto.setInvoiceType(invoice.getInvoiceType().name());
        }

        dto.setInvoiceNumber(invoice.getInvoiceNumber());
        dto.setGstPercent(invoice.getGstPercent());
        dto.setGstAmount(invoice.getGstAmount());
        dto.setAmountInclusive(invoice.getAmountInclusive());
        dto.setAmountExclusive(invoice.getAmountExclusive());
        dto.setTdsPercent(invoice.getTdsPercent());
        dto.setTdsAmount(invoice.getTdsAmount());
        dto.setFinalAmount(invoice.getFinalAmount());

        // Note: Your DTO has isGstInclusive field but Invoice entity doesn't.
        // If you want to keep it, you'll have to handle it separately.

        return dto;
    }
}
