package com.vendormanagement.vendor_management_system.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "vendor_services")
public class VendorService {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceType serviceType;

    @Column(name = "tds_rate", precision = 5, scale = 2)
    private BigDecimal tdsRate;

    // Constructors, getters, and setters
    public VendorService() {}

    public VendorService(Vendor vendor, ServiceType serviceType, BigDecimal tdsRate) {
        this.vendor = vendor;
        this.serviceType = serviceType;
        this.tdsRate = tdsRate;
    }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Vendor getVendor() { return vendor; }
    public void setVendor(Vendor vendor) { this.vendor = vendor; }

    public ServiceType getServiceType() { return serviceType; }
    public void setServiceType(ServiceType serviceType) { this.serviceType = serviceType; }

    public BigDecimal getTdsRate() { return tdsRate; }
    public void setTdsRate(BigDecimal tdsRate) { this.tdsRate = tdsRate; }
}