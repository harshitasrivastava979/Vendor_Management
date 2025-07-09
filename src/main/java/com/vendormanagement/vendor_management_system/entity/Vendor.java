package com.vendormanagement.vendor_management_system.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Vendor extends BaseEntity {

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
    private Boolean neftEnabled = false;
    private String beneficiaryCode;
    private Set<VendorService> vendorServices = new HashSet<>();

    // Constructors
    public Vendor() {}

    public Vendor(String name, String address, String contact) {
        this.name = name;
        this.address = address;
        this.contact = contact;
    }

    // All getters and setters remain the same...
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

    public String getBankAccType() { return bankAccType; }
    public void setBankAccType(String bankAccType) { this.bankAccType = bankAccType; }

    public Set<VendorService> getVendorServices() { return vendorServices; }
    public void setVendorServices(Set<VendorService> vendorServices) { this.vendorServices = vendorServices; }

    public void addService(ServiceType serviceType, BigDecimal tdsRate) {
        VendorService vendorService = new VendorService(this, serviceType, tdsRate);
        vendorServices.add(vendorService);
        serviceType.getVendorServices().add(vendorService);
    }

    public void removeService(ServiceType serviceType) {
        VendorService vendorService = vendorServices.stream()
                .filter(vs -> vs.getServiceType().equals(serviceType))
                .findFirst()
                .orElse(null);
        if (vendorService != null) {
            vendorServices.remove(vendorService);
            serviceType.getVendorServices().remove(vendorService);
            vendorService.setVendor(null);
            vendorService.setServiceType(null);
        }
    }
}