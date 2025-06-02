package com.vendormanagement.vendor_management_system.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class InvoiceRequestDto {

    private UUID vendorServiceId;
    private LocalDate month;
    private String invoiceType;
    private String invoiceNumber;

    // Original Inputs
    private BigDecimal amountExclusive;
    private BigDecimal gstPercent;
    private BigDecimal tdsPercent;
    private Boolean isGstInclusive;

    // Calculated Fields
    private BigDecimal gstAmount;
    private BigDecimal amountInclusive;
    private BigDecimal tdsAmount;
    private BigDecimal finalAmount;

    // Getter and Setter for vendorServiceId
    public UUID getVendorServiceId() {
        return vendorServiceId;
    }

    public void setVendorServiceId(UUID vendorServiceId) {
        this.vendorServiceId = vendorServiceId;
    }

    // Getter and Setter for month
    public LocalDate getMonth() {
        return month;
    }

    public void setMonth(LocalDate month) {
        this.month = month;
    }

    // Getter and Setter for invoiceType
    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    // Getter and Setter for invoiceNumber
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    // Getter and Setter for amountExclusive
    public BigDecimal getAmountExclusive() {
        return amountExclusive;
    }

    public void setAmountExclusive(BigDecimal amountExclusive) {
        this.amountExclusive = amountExclusive;
    }

    // Getter and Setter for gstPercent
    public BigDecimal getGstPercent() {
        return gstPercent;
    }

    public void setGstPercent(BigDecimal gstPercent) {
        this.gstPercent = gstPercent;
    }

    // Getter and Setter for tdsPercent
    public BigDecimal getTdsPercent() {
        return tdsPercent;
    }

    public void setTdsPercent(BigDecimal tdsPercent) {
        this.tdsPercent = tdsPercent;
    }

    // Getter and Setter for isGstInclusive
    public Boolean getIsGstInclusive() {
        return isGstInclusive;
    }

    public void setIsGstInclusive(Boolean isGstInclusive) {
        this.isGstInclusive = isGstInclusive;
    }

    // Getter and Setter for gstAmount
    public BigDecimal getGstAmount() {
        return gstAmount;
    }

    public void setGstAmount(BigDecimal gstAmount) {
        this.gstAmount = gstAmount;
    }

    // Getter and Setter for amountInclusive
    public BigDecimal getAmountInclusive() {
        return amountInclusive;
    }

    public void setAmountInclusive(BigDecimal amountInclusive) {
        this.amountInclusive = amountInclusive;
    }

    // Getter and Setter for tdsAmount
    public BigDecimal getTdsAmount() {
        return tdsAmount;
    }

    public void setTdsAmount(BigDecimal tdsAmount) {
        this.tdsAmount = tdsAmount;
    }

    // Getter and Setter for finalAmount
    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

    public void setFinalAmount(BigDecimal finalAmount) {
        this.finalAmount = finalAmount;
    }
}
