package com.vendormanagement.vendor_management_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

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

    // ✅ One-to-Many for detailed relationship with extra data
    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VendorService> vendorServices = new HashSet<>();

    // Constructors
    public Vendor() {}

    public Vendor(String name, String address, String contact) {
        this.name = name;
        this.address = address;
        this.contact = contact;
    }

    // Getters & Setters
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

    public void setVendorServices(Set<VendorService> vendorServices) {
        this.vendorServices = vendorServices;
    }

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
