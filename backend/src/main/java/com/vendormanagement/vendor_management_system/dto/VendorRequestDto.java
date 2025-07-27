package com.vendormanagement.vendor_management_system.dto;

import com.vendormanagement.vendor_management_system.entity.VendorService;

import java.math.BigDecimal;
import java.util.*;

public class VendorRequestDto {
    private String name;
    private String address;
    private String contact;
    private String gst;
    private String tan;
    private String cin;
    private String bankAcc;// drop down saving /current
    private String ifsc;
    private Boolean neftEnabled;
    List<VendorServiceRequestDto> vendorServiceList ;
    private String bankName;

    private String branchAdd;
    private String beneficiaryCode;
    private String bankAccType; // saving or current

    //    static List<VendorServiceRequestDto> vendorServiceList ;
    public List<VendorServiceRequestDto> getVendorServiceList() {
        return vendorServiceList;
    }

    public void setVendorServiceList(List<VendorServiceRequestDto> vendorServiceList) {
        this.vendorServiceList = vendorServiceList;
    }
    public String getBankAccType() {
        return bankAccType;
    }
    public void setBankAccType(String bankAccType) {
        this.bankAccType = bankAccType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGst() {
        return gst;
    }

    public String getTan() {
        return tan;
    }

    public String getCin() {
        return cin;
    }

    public void setGst(String gst) {
        this.gst= gst;
    }

    public void setTan(String tan) {
        this.tan= tan;
    }

    public void setCin(String cin) {
        this.cin= cin;
    }

    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchAdd() {
        return branchAdd;
    }

    public void setBranchAdd(String branchAdd) {
        this.branchAdd = branchAdd;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public Boolean getNeftEnabled() {
        return neftEnabled;
    }

    public void setNeftEnabled(Boolean neftEnabled) {
        this.neftEnabled = neftEnabled;
    }

    public String getBeneficiaryCode() {
        return beneficiaryCode;
    }

    public void setBeneficiaryCode(String beneficiaryCode) {
        this.beneficiaryCode = beneficiaryCode;
    }
}


