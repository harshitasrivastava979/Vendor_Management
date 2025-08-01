package com.vendormanagement.vendor_management_system.service;

import com.vendormanagement.vendor_management_system.dto.VendorRequestDto;
import com.vendormanagement.vendor_management_system.dto.VendorServiceRequestDto;
import com.vendormanagement.vendor_management_system.entity.ServiceType;
import com.vendormanagement.vendor_management_system.entity.Vendor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface VendorServiceHandler {
    Vendor createVendor(VendorRequestDto dto);
    Vendor getVendorbyId(UUID id);
    void deleteVendor(UUID id);


    List<VendorServiceRequestDto> getVendorsWithServicesAssignment();

    Vendor updateVendor(UUID id, VendorRequestDto dto);

    // List<VendorServiceRequestDto> getVendorWithServices();
}






