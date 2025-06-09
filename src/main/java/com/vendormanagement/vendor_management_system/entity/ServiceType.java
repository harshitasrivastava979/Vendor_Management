package com.vendormanagement.vendor_management_system.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "service_types")
public class ServiceType extends BaseEntity {

    @NotBlank(message = "Service type name is required")
    @Column(nullable = false, unique = true)
    private String name;


    @OneToMany(mappedBy = "serviceType", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("service-vendors") // This manages the relationship
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