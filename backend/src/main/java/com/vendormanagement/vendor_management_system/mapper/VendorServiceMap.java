package com.vendormanagement.vendor_management_system.mapper;

import com.vendormanagement.vendor_management_system.dto.VendorServiceRequestDto;
import com.vendormanagement.vendor_management_system.dto.VendorServiceResponseDto;
import com.vendormanagement.vendor_management_system.entity.ServiceType;
import com.vendormanagement.vendor_management_system.entity.Vendor;
import com.vendormanagement.vendor_management_system.entity.VendorService;
import com.vendormanagement.vendor_management_system.repository.ServiceTypeRepository;
import com.vendormanagement.vendor_management_system.repository.VendorRepository;
import com.vendormanagement.vendor_management_system.repository.VendorServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VendorServiceMap {

    private static VendorRepository vendorRepository;
    private static ServiceTypeRepository serviceTypeRepository;
    private static VendorServiceRepository vendorServiceRepository;

    @Autowired
    public VendorServiceMap(VendorRepository vendorRepository,
                            ServiceTypeRepository serviceTypeRepository,
                            VendorServiceRepository vendorServiceRepository) {
        VendorServiceMap.vendorRepository = vendorRepository;
        VendorServiceMap.serviceTypeRepository = serviceTypeRepository;
        VendorServiceMap.vendorServiceRepository = vendorServiceRepository;
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

    public static VendorServiceResponseDto toDto(VendorService vendorService) {
        VendorServiceResponseDto dto = new VendorServiceResponseDto();

        dto.setId(vendorService.getId());
        dto.setVendorId(vendorService.getVendor().getId());
        dto.setVendorName(vendorService.getVendor().getName());

        VendorServiceResponseDto.ServiceTypeDto serviceTypeDto = new VendorServiceResponseDto.ServiceTypeDto();
        serviceTypeDto.setId(vendorService.getServiceType().getId());
        serviceTypeDto.setName(vendorService.getServiceType().getName());

        dto.setServiceType(serviceTypeDto);
        dto.setTdsRate(vendorService.getTdsRate());
        dto.setVendorContact(vendorService.getVendor().getContact());
        dto.setVendorGst(vendorService.getVendor().getGst());
        dto.setVendorTan(vendorService.getVendor().getTan());
        dto.setVendorCin(vendorService.getVendor().getCin());
        dto.setCreatedAt(vendorService.getCreatedAt());
        
        // Check if this vendor service is used in invoices
        boolean usedInInvoices = vendorServiceRepository.isUsedInInvoices(vendorService.getId());
        dto.setUsedInInvoices(usedInInvoices);

        return dto;
    }

}