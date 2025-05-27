package com.vendormanagement.vendor_management_system.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "vendors")
public class Vendor extends BaseEntity {

    @NotBlank(message = "Vendor name is required")
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String contact;

    @Column(name = "gst_tan_cin")
    private String gstTanCin;

    @Column(name = "bank_acc")
    private String bankAcc;

    @Column(name = "ifsc")
    private String ifsc;

    @Column(name = "neft_enabled")
    private Boolean neftEnabled = false;

    // Constructors
    public Vendor() {}

    public Vendor(String name, String address, String contact) {
        this.name = name;
        this.address = address;
        this.contact = contact;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getGstTanCin() { return gstTanCin; }
    public void setGstTanCin(String gstTanCin) { this.gstTanCin = gstTanCin; }

    public String getBankAcc() { return bankAcc; }
    public void setBankAcc(String bankAcc) { this.bankAcc = bankAcc; }

    public String getIfsc() { return ifsc; }
    public void setIfsc(String ifsc) { this.ifsc = ifsc; }

    public Boolean getNeftEnabled() { return neftEnabled; }
    public void setNeftEnabled(Boolean neftEnabled) { this.neftEnabled = neftEnabled; }
}