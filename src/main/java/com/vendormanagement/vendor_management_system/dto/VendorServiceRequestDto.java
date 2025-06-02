package com.vendormanagement.vendor_management_system.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class VendorServiceRequestDto {
    private UUID vendorId;
    private UUID serviceTypeId;
    private BigDecimal tdsRate;

    // Getters and Setters
    public UUID getVendorId() { return vendorId; }
    public void setVendorId(UUID vendorId) { this.vendorId = vendorId; }

    public UUID getServiceTypeId() { return serviceTypeId; }
    public void setServiceTypeId(UUID serviceTypeId) { this.serviceTypeId = serviceTypeId; }

    public BigDecimal getTdsRate() { return tdsRate; }
    public void setTdsRate(BigDecimal tdsRate) { this.tdsRate = tdsRate; }
}