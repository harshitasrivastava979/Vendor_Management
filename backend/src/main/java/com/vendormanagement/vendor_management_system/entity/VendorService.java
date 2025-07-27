package com.vendormanagement.vendor_management_system.entity;

import java.math.BigDecimal;

public class VendorService extends BaseEntity {

//    , uniqueConstraints = @UniqueConstraint(columnNames = {"vendor_id", "service_id"}

    private Vendor vendor;
    private ServiceType serviceType;
    private BigDecimal tdsRate;

    // Transient field to track if this service is used in invoices
    private Boolean usedInInvoices;

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

    public Boolean getUsedInInvoices() { return usedInInvoices; }
    public void setUsedInInvoices(Boolean usedInInvoices) { this.usedInInvoices = usedInInvoices; }
}