package com.vendormanagement.vendor_management_system.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class VendorResponseDto {
    private UUID id;
    private String name;
    private String address;
    private String contact;
    private String gst;
    private String tan;
    private String cin;
    private String bankAcc;
    private String bankAccType;
    private String bankName;
    private String branchAdd;
    private String ifsc;
    private Boolean neftEnabled;
    private String beneficiaryCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<VendorServiceResponseDto> vendorServiceList;

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getGst() { return gst; }
    public void setGst(String gst) { this.gst = gst; }

    public String getTan() { return tan; }
    public void setTan(String tan) { this.tan = tan; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public String getBankAcc() { return bankAcc; }
    public void setBankAcc(String bankAcc) { this.bankAcc = bankAcc; }

    public String getBankAccType() { return bankAccType; }
    public void setBankAccType(String bankAccType) { this.bankAccType = bankAccType; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getBranchAdd() { return branchAdd; }
    public void setBranchAdd(String branchAdd) { this.branchAdd = branchAdd; }

    public String getIfsc() { return ifsc; }
    public void setIfsc(String ifsc) { this.ifsc = ifsc; }

    public Boolean getNeftEnabled() { return neftEnabled; }
    public void setNeftEnabled(Boolean neftEnabled) { this.neftEnabled = neftEnabled; }

    public String getBeneficiaryCode() { return beneficiaryCode; }
    public void setBeneficiaryCode(String beneficiaryCode) { this.beneficiaryCode = beneficiaryCode; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<VendorServiceResponseDto> getVendorServiceList() { return vendorServiceList; }
    public void setVendorServiceList(List<VendorServiceResponseDto> vendorServiceList) { this.vendorServiceList = vendorServiceList; }
} 