package com.vendormanagement.vendor_management_system.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "vendor_services")
public class VendorService extends BaseEntity {

//    , uniqueConstraints = @UniqueConstraint(columnNames = {"vendor_id", "service_id"}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    @JsonBackReference("vendor-services") // This prevents circular reference
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    @JsonBackReference("service-vendors") // This prevents circular reference
    private ServiceType serviceType;

    @Column(name = "tds_rate", precision = 5, scale = 2)
    private BigDecimal tdsRate;

    // Constructors
    public VendorService() {}

    public VendorService(Vendor vendor, ServiceType serviceType, BigDecimal tdsRate) {
        this.vendor = vendor;
        this.serviceType = serviceType;
        this.tdsRate = tdsRate;
    }

    // Getters and setters
    public Vendor getVendor() { return vendor; }
    public void setVendor(Vendor vendor) { this.vendor = vendor; }

    public ServiceType getServiceType() { return serviceType; }
    public void setServiceType(ServiceType serviceType) { this.serviceType = serviceType; }

    public BigDecimal getTdsRate() { return tdsRate; }
    public void setTdsRate(BigDecimal tdsRate) { this.tdsRate = tdsRate; }
}