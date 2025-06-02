package com.vendormanagement.vendor_management_system.service;

import com.vendormanagement.vendor_management_system.dto.VendorServiceRequestDto;
import com.vendormanagement.vendor_management_system.entity.VendorService;

public interface VendorServiceService {
    VendorService createVendorService(VendorServiceRequestDto dto);
}