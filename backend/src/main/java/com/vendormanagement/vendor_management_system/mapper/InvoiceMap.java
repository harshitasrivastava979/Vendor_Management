package com.vendormanagement.vendor_management_system.mapper;

import com.vendormanagement.vendor_management_system.dto.InvoiceExcelDto;
import com.vendormanagement.vendor_management_system.dto.InvoiceRequestDto;
import com.vendormanagement.vendor_management_system.entity.Invoice;
import com.vendormanagement.vendor_management_system.entity.Vendor;
import com.vendormanagement.vendor_management_system.entity.VendorService;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMap {

    public Invoice mapDtoToInvoice(InvoiceRequestDto dto, VendorService vendorService) {
        Invoice invoice = new Invoice();

        invoice.setVendorService(vendorService);
        invoice.setInvoiceMonth(dto.getInvoiceMonth());
        invoice.setTransactionDate(dto.getTransactionDate());


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
        invoice.setBillFileKey(dto.getBillFileKey());

        // Map payment fields
        if (dto.getPaymentMethod() != null) {
            invoice.setPaymentMethod(Invoice.PaymentMethod.valueOf(dto.getPaymentMethod()));
        }
        if (dto.getPaymentStatus() != null) {
            invoice.setPaymentStatus(Invoice.PaymentStatus.valueOf(dto.getPaymentStatus()));
        }

        return invoice;
    }


//    public InvoiceRequestDto mapInvoiceToDto(Invoice invoice) {
//        InvoiceRequestDto dto = new InvoiceRequestDto();
//
//        dto.setVendorServiceId(invoice.getVendorService().getId());
//        dto.setInvoiceMonth(invoice.getInvoiceMonth());
//
//        // Convert InvoiceType enum to String
//        if (invoice.getInvoiceType() != null) {
//            dto.setInvoiceType(invoice.getInvoiceType().name());
//        }
//
//        dto.setInvoiceNumber(invoice.getInvoiceNumber());
//        dto.setGstPercent(invoice.getGstPercent());
//        dto.setGstAmount(invoice.getGstAmount());
//        dto.setAmountInclusive(invoice.getAmountInclusive());
//        dto.setAmountExclusive(invoice.getAmountExclusive());
//        dto.setTdsPercent(invoice.getTdsPercent());
//        dto.setTdsAmount(invoice.getTdsAmount());
//        dto.setFinalAmount(invoice.getFinalAmount());
//
//        // Map payment fields
//        if (invoice.getPaymentMethod() != null) {
//            dto.setPaymentMethod(invoice.getPaymentMethod().name());
//        }
//        if (invoice.getPaymentStatus() != null) {
//            dto.setPaymentStatus(invoice.getPaymentStatus().name());
//        }
//
//        // Note: Your DTO has isGstInclusive field but Invoice entity doesn't.
//        // If you want to keep it, you'll have to handle it separately.
//
//        return dto;
//    }
//
//    public InvoiceExcelDto mapToExcelDto(Invoice invoice) {
//        Vendor vendor = invoice.getVendorService().getVendor();
//
//        InvoiceExcelDto dto = new InvoiceExcelDto();
//        dto.setTransactionType(invoice.getTransactionType());//n , r ,i
//        dto.setBeneficiaryCode(vendor.getBeneficiaryCode()); // will be updated
//        dto.setBeneAccNum(vendor.getBankAcc()); //getAccNum
//
//        dto.setBeneName(vendor.getName());
//        dto.setBeneAddress(vendor.getAddress());
//        dto.setIfscCode(vendor.getIfsc());
//        dto.setBranchName(vendor.getBranchAdd());
//        dto.setBankName(vendor.getBankName());
//        dto.setAmountToBeTransferred(invoice.getFinalAmount());
//        dto.setCustomerRefNo(invoice.getInvoiceNumber());
//        dto.setTransactionDate(invoice.getTransactionDate());// when transaction date will be added, update this
//
//
//        return dto;
//    }
public InvoiceExcelDto mapToExcelDto(Invoice invoice) {
    InvoiceExcelDto dto = new InvoiceExcelDto();

    // Map all required fields from Invoice (and related entities) to InvoiceExcelDto
    dto.setTransactionType(invoice.getPaymentMethod());
    dto.setTransactionDate(invoice.getTransactionDate());
    dto.setInvoiceMonth(invoice.getInvoiceMonth());
    dto.setAmountToBeTransferred(invoice.getFinalAmount());
    //  dto.setCustomerRefNo(invoice.getInvoiceNumber());

    //You need to map these from related entities (Vendor, Bank, etc.)
    // Example (pseudo-code, adjust as per your actual model):
    Vendor vendor = invoice.getVendorService().getVendor();
    dto.setBeneName(vendor.getName());
    dto.setBeneAccNum(vendor.getBankAcc());
    dto.setIfscCode(vendor.getIfsc());
    dto.setBankName(vendor.getBankName());
    dto.setBranchName(vendor.getBranchAdd());
    dto.setBeneficiaryCode(vendor.getBeneficiaryCode());
    dto.setBeneAddress(vendor.getAddress());
    dto.setCustomerRefNo(invoice.getInvoiceNumber());

    // Fill in the rest as per your data model

    return dto;
}

    public InvoiceRequestDto mapInvoiceToDto(Invoice invoice) {
        InvoiceRequestDto dto = new InvoiceRequestDto();

        dto.setVendorServiceId(invoice.getVendorService().getId());
        dto.setInvoiceMonth(invoice.getInvoiceMonth());

        // Convert InvoiceType enum to String
        if (invoice.getInvoiceType() != null) {
            dto.setInvoiceType(invoice.getInvoiceType().name());
        }
        dto.setTransactionDate(invoice.getTransactionDate());

        dto.setInvoiceNumber(invoice.getInvoiceNumber());
        dto.setGstPercent(invoice.getGstPercent());
        dto.setGstAmount(invoice.getGstAmount());
        dto.setAmountInclusive(invoice.getAmountInclusive());
        dto.setAmountExclusive(invoice.getAmountExclusive());
        dto.setTdsPercent(invoice.getTdsPercent());
        dto.setTdsAmount(invoice.getTdsAmount());
        dto.setFinalAmount(invoice.getFinalAmount());
        dto.setId(invoice.getId());
        dto.setBillFileKey(invoice.getBillFileKey());

        // Map payment fields
        if (invoice.getPaymentMethod() != null) {
            dto.setPaymentMethod(invoice.getPaymentMethod().name());
        }
        if (invoice.getPaymentStatus() != null) {
            dto.setPaymentStatus(invoice.getPaymentStatus().name());
        }

        // Note: Your DTO has isGstInclusive field but Invoice entity doesn't.
        // If you want to keep it, you'll have to handle it separately.

        return dto;
    }

}
