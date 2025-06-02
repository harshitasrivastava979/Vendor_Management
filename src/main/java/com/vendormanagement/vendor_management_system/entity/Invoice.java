package com.vendormanagement.vendor_management_system.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "invoices")
public class Invoice extends BaseEntity {

    public enum InvoiceType {
        STANDARD,
        AVAILABLE,
        NOT_AVAILABLE
    }

    @Column(name = "invoice_month", nullable = false)
    private LocalDate invoiceMonth;

    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_type", nullable = false)
    private InvoiceType invoiceType;

    @Column(name = "invoice_number", nullable = false, unique = true)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendorservice_id", nullable = false)
    private VendorService vendorService;

    @Column(name = "gst_percent", precision = 5, scale = 2)
    private BigDecimal gstPercent;

    @Column(name = "gst_amount", precision = 10, scale = 2)
    private BigDecimal gstAmount;

    @Column(name = "amount_inclusive", precision = 10, scale = 2)
    private BigDecimal amountInclusive;

    @Column(name = "amount_exclusive", precision = 10, scale = 2)
    private BigDecimal amountExclusive;

    @Column(name = "tds_percent", precision = 5, scale = 2)
    private BigDecimal tdsPercent;

    @Column(name = "tds_amount", precision = 10, scale = 2)
    private BigDecimal tdsAmount;

    @Column(name = "final_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal finalAmount;

    // Constructors
    public Invoice() {}

    public Invoice(LocalDate invoiceMonth, InvoiceType invoiceType,
                   String invoiceNumber, VendorService vendorService,
                   BigDecimal finalAmount) {
        this.invoiceMonth = invoiceMonth;
        this.invoiceType = invoiceType;
        this.invoiceNumber = invoiceNumber;
        this.vendorService = vendorService;
        this.finalAmount = finalAmount;
    }

    // Getters and Setters
    public LocalDate getInvoiceMonth() { return invoiceMonth; }
    public void setInvoiceMonth(LocalDate invoiceMonth) { this.invoiceMonth = invoiceMonth; }

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

}