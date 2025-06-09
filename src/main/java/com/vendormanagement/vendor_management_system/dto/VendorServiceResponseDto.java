package com.vendormanagement.vendor_management_system.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class VendorServiceResponseDto {
    private UUID id;
    private String vendorName;
    private ServiceTypeDto serviceType;
    private BigDecimal tdsRate;
    private String vendorContact;
    private String vendorGstTanCin;
    private LocalDateTime createdAt;

    public void setServiceTypeName(String name) {
        
    }

    @Data
    public static class ServiceTypeDto {
        private UUID id;
        private String name;
    }
} 