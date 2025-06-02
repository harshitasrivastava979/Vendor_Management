package com.vendormanagement.vendor_management_system.service.implementation;

import com.vendormanagement.vendor_management_system.dto.VendorServiceRequestDto;
import com.vendormanagement.vendor_management_system.entity.VendorService;
import com.vendormanagement.vendor_management_system.mapper.VendorServiceMap;
import com.vendormanagement.vendor_management_system.repository.VendorServiceRepository;
import com.vendormanagement.vendor_management_system.service.VendorServiceService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
public class VendorServiceImp implements VendorServiceService {

    private final VendorServiceRepository vendorServiceRepository;

    public VendorServiceImp(VendorServiceRepository vendorServiceRepository) {
        this.vendorServiceRepository = vendorServiceRepository;
    }

    @Override
    public VendorService createVendorService(VendorServiceRequestDto dto) {
        // Check if relationship already exists
        if (vendorServiceRepository.existsByVendorIdAndServiceTypeId(
                dto.getVendorId(), dto.getServiceTypeId())) {
            throw new RuntimeException("Service already exists for this vendor");
        }

        VendorService vendorService = VendorServiceMap.mapToEntity(dto);
        return vendorServiceRepository.save(vendorService);
    }
}