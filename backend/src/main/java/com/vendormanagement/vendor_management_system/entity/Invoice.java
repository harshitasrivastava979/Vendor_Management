package com.vendormanagement.vendor_management_system.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Invoice extends BaseEntity {

    public enum InvoiceType {
        STANDARD,
        AVAILABLE,
        NOT_AVAILABLE
    }

    public enum PaymentMethod {
        NEFT,
        RTGS,
        IFT
    }

    public enum PaymentStatus {
        PENDING,
        PAID,
        OVERDUE
    }

    private LocalDate transactionDate;
    private String invoiceMonth;
    private InvoiceType invoiceType;
    private String invoiceNumber;
    private VendorService vendorService;
    private BigDecimal gstPercent;
    private BigDecimal gstAmount;
    private BigDecimal amountInclusive;
    private BigDecimal amountExclusive;
    private BigDecimal tdsPercent;
    private BigDecimal tdsAmount;
    private BigDecimal finalAmount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String billFileKey; // e.g. "bills/invoice-123.pdf"

    // Constructors
    public Invoice() {}

    public Invoice(LocalDate transactionDate, InvoiceType invoiceType,
                   String invoiceNumber, VendorService vendorService,
                   BigDecimal finalAmount) {
        this.transactionDate = transactionDate;
        this.invoiceType = invoiceType;
        this.invoiceNumber = invoiceNumber;
        this.vendorService = vendorService;
        this.finalAmount = finalAmount;
    }

    public String getInvoiceMonth() {
        return invoiceMonth;
    }

    public void setInvoiceMonth(String invoiceMonth) {
        this.invoiceMonth = invoiceMonth;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate tansactionDate) {
        this.transactionDate = tansactionDate;
    }

    public InvoiceType getInvoiceType() { return invoiceType; }
    public void setInvoiceType(InvoiceType invoiceType) { this.invoiceType = invoiceType; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public VendorService getVendorService() { return vendorService; }
    public void setVendorService(VendorService vendorService) { this.vendorService = vendorService; }

    public BigDecimal getGstPercent() { return gstPercent; }
    public void setGstPercent(BigDecimal gstPercent) { this.gstPercent = gstPercent; }

    public BigDecimal getGstAmount() { return gstAmount; }
    public void setGstAmount(BigDecimal gstAmount) { this.gstAmount = gstAmount; }

    public BigDecimal getAmountInclusive() { return amountInclusive; }
    public void setAmountInclusive(BigDecimal amountInclusive) { this.amountInclusive = amountInclusive; }

    public BigDecimal getAmountExclusive() { return amountExclusive; }
    public void setAmountExclusive(BigDecimal amountExclusive) { this.amountExclusive = amountExclusive; }

    public BigDecimal getTdsPercent() { return tdsPercent; }
    public void setTdsPercent(BigDecimal tdsPercent) { this.tdsPercent = tdsPercent; }

    public BigDecimal getTdsAmount() { return tdsAmount; }
    public void setTdsAmount(BigDecimal tdsAmount) { this.tdsAmount = tdsAmount; }

    public BigDecimal getFinalAmount() { return finalAmount; }
    public void setFinalAmount(BigDecimal finalAmount) { this.finalAmount = finalAmount; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getBillFileKey() {
        return billFileKey;
    }

    public void setBillFileKey(String billFileKey) {
        this.billFileKey = billFileKey;
    }
}