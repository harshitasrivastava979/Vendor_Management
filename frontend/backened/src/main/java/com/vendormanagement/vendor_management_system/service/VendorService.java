package com.vendormanagement.vendor_management_system.service;

import com.vendormanagement.vendor_management_system.dto.VendorRequestDto;
import com.vendormanagement.vendor_management_system.entity.Vendor;

public interface VendorService {
    Vendor createVendor(VendorRequestDto dto);
}






