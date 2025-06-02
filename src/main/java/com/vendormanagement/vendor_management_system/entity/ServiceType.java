package com.vendormanagement.vendor_management_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "service_types")
public class ServiceType extends BaseEntity {

    @NotBlank(message = "Service type name is required")
    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1000)
    private String description;

//    // ✅ Many-to-Many for basic relationship
//    @ManyToMany(mappedBy = "serviceTypes")
//    private Set<Vendor> vendors = new HashSet<>();

    // ✅ One-to-Many for detailed relationship with extra data
    @OneToMany(mappedBy = "serviceType", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VendorService> vendorServices = new HashSet<>();

    // Constructors
    public ServiceType() {}

    public ServiceType(String name) {
        this.name = name;
    }

    // Getters & Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

//    public Set<Vendor> getVendors() { return vendors; }
//    public void setVendors(Set<Vendor> vendors) { this.vendors = vendors; }

    public Set<VendorService> getVendorServices() { return vendorServices; }
    public void setVendorServices(Set<VendorService> vendorServices) {
        this.vendorServices = vendorServices;
    }

//    public void addVendor(Vendor vendor, BigDecimal tdsRate) {
//        VendorService vendorService = new VendorService(vendor, this, tdsRate);
//        vendorServices.add(vendorService);
//        vendor.getVendorServices().add(vendorService);
//    }
//
//    public void removeVendor(Vendor vendor) {
//        VendorService vendorService = vendorServices.stream()
//                .filter(vs -> vs.getVendor().equals(vendor))
//                .findFirst()
//                .orElse(null);
//        if (vendorService != null) {
//            vendorServices.remove(vendorService);
//            vendor.getVendorServices().remove(vendorService);
//            vendorService.setVendor(null);
//            vendorService.setServiceType(null);
//        }
//    }
}
