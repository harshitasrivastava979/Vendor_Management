package com.vendormanagement.vendor_management_system.service.implementation;

import com.vendormanagement.vendor_management_system.dto.VendorRequestDto;
import com.vendormanagement.vendor_management_system.dto.VendorServiceRequestDto;
import com.vendormanagement.vendor_management_system.entity.ServiceType;
import com.vendormanagement.vendor_management_system.entity.Vendor;
import com.vendormanagement.vendor_management_system.entity.VendorService;
import com.vendormanagement.vendor_management_system.mapper.VendorMap;
import com.vendormanagement.vendor_management_system.repository.ServiceTypeRepository;
import com.vendormanagement.vendor_management_system.repository.VendorRepository;
import com.vendormanagement.vendor_management_system.service.VendorServiceHandler;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public  class  VendorImp implements VendorServiceHandler {
    //dependency injection
    private  VendorRepository vendorRepository;
    private  ServiceTypeRepository serviceTypeRepository;


    public VendorImp(VendorRepository vendorRepository ,ServiceTypeRepository serviceTypeRepository) {
        this.vendorRepository = vendorRepository;
        this.serviceTypeRepository = serviceTypeRepository;
    }

    @Override
    public Vendor createVendor(VendorRequestDto dto) {
        // Map basic vendor fields first
        Vendor vendor = VendorMap.mapToEntity(dto);

        // Save vendor first so it has an ID
        vendor = vendorRepository.save(vendor);

        // Now handle VendorService relations
        if (dto.getVendorServiceList() != null) {
            Set<com.vendormanagement.vendor_management_system.entity.VendorService> vendorServices = new HashSet<>();
            for (VendorServiceRequestDto vsDto : dto.getVendorServiceList()) {
                ServiceType serviceType = serviceTypeRepository.findById(vsDto.getServiceTypeId())
                        .orElseThrow(() -> new RuntimeException("ServiceType not found with id: " + vsDto.getServiceTypeId()));

                VendorService vendorService = new VendorService();
                vendorService.setVendor(vendor);
                vendorService.setServiceType(serviceType);
                vendorService.setTdsRate(vsDto.getTdsRate());

                vendorServices.add(vendorService);
            }

            // Set vendorServices on vendor entity
            vendor.setVendorServices(vendorServices);

            // Save vendor again to persist the VendorServices (Cascade.ALL or explicit save)
            vendor = vendorRepository.save(vendor);
        }

        return vendor;
    }


    @Override
    public Vendor getVendorbyId(UUID id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found with id: " + id));
    }
    @Override
    public void deleteVendor(UUID id){
        vendorRepository.delete(getVendorbyId(id));
    }

}
