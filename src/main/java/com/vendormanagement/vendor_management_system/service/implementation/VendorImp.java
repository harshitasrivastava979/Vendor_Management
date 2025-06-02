package com.vendormanagement.vendor_management_system.service.implementation;

import com.vendormanagement.vendor_management_system.dto.VendorRequestDto;
import com.vendormanagement.vendor_management_system.entity.Vendor;
import com.vendormanagement.vendor_management_system.mapper.VendorMap;
import com.vendormanagement.vendor_management_system.repository.VendorRepository;
import com.vendormanagement.vendor_management_system.service.VendorService;
import org.springframework.stereotype.Service;

@Service

public  class VendorImp implements VendorService {
    //dependency injection
    private  VendorRepository vendorRepository;

    public VendorImp(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public Vendor createVendor(VendorRequestDto dto) {
        Vendor vendor = VendorMap.mapToEntity(dto);
        return vendorRepository.save(vendor);
    }
}
