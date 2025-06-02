package com.vendormanagement.vendor_management_system.mapper;

import com.vendormanagement.vendor_management_system.dto.VendorServiceRequestDto;
import com.vendormanagement.vendor_management_system.entity.ServiceType;
import com.vendormanagement.vendor_management_system.entity.Vendor;
import com.vendormanagement.vendor_management_system.entity.VendorService;
import com.vendormanagement.vendor_management_system.repository.ServiceTypeRepository;
import com.vendormanagement.vendor_management_system.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VendorServiceMap {

    private static VendorRepository vendorRepository;
    private static ServiceTypeRepository serviceTypeRepository;

    @Autowired
    public VendorServiceMap(VendorRepository vendorRepository,
                            ServiceTypeRepository serviceTypeRepository) {
        VendorServiceMap.vendorRepository = vendorRepository;
        VendorServiceMap.serviceTypeRepository = serviceTypeRepository;
    }

    public static VendorService mapToEntity(VendorServiceRequestDto dto) {
        Vendor vendor = vendorRepository.findById(dto.getVendorId())
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        ServiceType serviceType = serviceTypeRepository.findById(dto.getServiceTypeId())
                .orElseThrow(() -> new RuntimeException("ServiceType not found"));

        VendorService vendorService = new VendorService();
        vendorService.setVendor(vendor);
        vendorService.setServiceType(serviceType);
        vendorService.setTdsRate(dto.getTdsRate());

        return vendorService;
    }
}