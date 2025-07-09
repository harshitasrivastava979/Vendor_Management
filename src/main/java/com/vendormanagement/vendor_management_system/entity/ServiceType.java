package com.vendormanagement.vendor_management_system.entity;

import java.util.HashSet;
import java.util.Set;

public class ServiceType extends BaseEntity {

    private String name;
    private Set<VendorService> vendorServices = new HashSet<>();

    // Constructors
    public ServiceType() {}

    public ServiceType(String name) {
        this.name = name;
    }

    // Getters & Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<VendorService> getVendorServices() { return vendorServices; }
    public void setVendorServices(Set<VendorService> vendorServices) {
        this.vendorServices = vendorServices;
    }
}