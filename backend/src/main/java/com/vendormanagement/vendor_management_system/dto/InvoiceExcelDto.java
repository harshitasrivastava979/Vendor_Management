package com.vendormanagement.vendor_management_system.dto;

import com.vendormanagement.vendor_management_system.entity.Invoice.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvoiceExcelDto {

    private PaymentMethod transactionType;
    private String beneficiaryCode;
    private String beneAccNum;
    private String beneName;
    private String beneAddress;
    private String ifscCode;
    private String branchName;
    private String bankName;
    private BigDecimal amountToBeTransferred;
    private String customerRefNo;
    private LocalDate transactionDate;
    private String invoiceMonth;

    // No-args constructor
    public InvoiceExcelDto() {
    }

    // Getters and Setters
    public PaymentMethod getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(PaymentMethod transactionType) {
        this.transactionType = transactionType;
    }

    public String getBeneficiaryCode() {
        return beneficiaryCode;
    }

    public void setBeneficiaryCode(String beneficiaryCode) {
        this.beneficiaryCode = beneficiaryCode;
    }

    public String getBeneAccNum() {
        return beneAccNum;
    }

    public void setBeneAccNum(String beneAccName) {
        this.beneAccNum = beneAccName;
    }

    public String getBeneName() {
        return beneName;
    }

    public void setBeneName(String beneName) {
        this.beneName = beneName;
    }

    public String getBeneAddress() {
        return beneAddress;
    }

    public void setBeneAddress(String beneAddress) {
        this.beneAddress = beneAddress;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public BigDecimal getAmountToBeTransferred() {
        return amountToBeTransferred;
    }

    public void setAmountToBeTransferred(BigDecimal amountToBeTransferred) {
        this.amountToBeTransferred = amountToBeTransferred;
    }

    public String getCustomerRefNo() {
        return customerRefNo;
    }

    public void setCustomerRefNo(String customerRefNo) {
        this.customerRefNo = customerRefNo;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getInvoiceMonth() {
        return invoiceMonth;
    }

    public void setInvoiceMonth(String invoiceMonth) {
        this.invoiceMonth = invoiceMonth;

    }
}