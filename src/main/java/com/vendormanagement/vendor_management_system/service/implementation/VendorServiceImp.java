package com.vendormanagement.vendor_management_system.service.implementation;

import com.vendormanagement.vendor_management_system.dto.VendorServiceRequestDto;
import com.vendormanagement.vendor_management_system.dto.VendorServiceResponseDto;
import com.vendormanagement.vendor_management_system.entity.ServiceType;
import com.vendormanagement.vendor_management_system.entity.Vendor;
import com.vendormanagement.vendor_management_system.entity.VendorService;
import com.vendormanagement.vendor_management_system.mapper.VendorServiceMap;
import com.vendormanagement.vendor_management_system.repository.ServiceTypeRepository;
import com.vendormanagement.vendor_management_system.repository.VendorRepository;
import com.vendormanagement.vendor_management_system.repository.VendorServiceRepository;
import com.vendormanagement.vendor_management_system.service.VendorServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VendorServiceImp implements VendorServiceService {

    @Autowired
    private VendorServiceRepository vendorServiceRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private VendorServiceMap vendorServiceMap;

    @Override
    public VendorServiceResponseDto createVendorService(VendorServiceRequestDto requestDto) {
        try {
            Vendor vendor = vendorRepository.findById(requestDto.getVendorId())
                    .orElseThrow(() -> new RuntimeException("Vendor not found"));

            ServiceType serviceType = serviceTypeRepository.findById(requestDto.getServiceTypeId())
                    .orElseThrow(() -> new RuntimeException("Service Type not found"));

            // Check if vendor service already exists
            List<VendorService> existingServices = vendorServiceRepository.findByVendorIdAndServiceTypeId(
                    requestDto.getVendorId(), requestDto.getServiceTypeId());

            if (!existingServices.isEmpty()) {
                throw new IllegalArgumentException("Vendor is already assigned to this service type.");
            }

            VendorService vendorService = new VendorService();
            vendorService.setVendor(vendor);
            vendorService.setServiceType(serviceType);
            vendorService.setTdsRate(requestDto.getTdsRate());

            VendorService savedVendorService = vendorServiceRepository.save(vendorService);
            return convertToResponseDto(savedVendorService);

        } catch (Exception e) {
            if (e.getMessage().contains("already assigned")) {
                throw e;
            }
            throw new RuntimeException("Error creating vendor service: " + e.getMessage(), e);
        }
    }

    @Override
    public VendorServiceResponseDto updateVendorService(UUID id, VendorServiceRequestDto dto) {
        VendorService existing = vendorServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VendorService not found"));

        // Using the same logic as create (static map)
        VendorService updatedEntity = VendorServiceMap.mapToEntity(dto);
        updatedEntity.setId(id); // Retain the original ID
        VendorService saved = vendorServiceRepository.save(updatedEntity);

        return VendorServiceMap.toDto(saved);
    }

    @Override
    public void deleteVendorService(UUID id) {
        VendorService existing = vendorServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VendorService not found"));
        vendorServiceRepository.delete(existing);
    }

    @Override
    public List<VendorServiceResponseDto> getVendorServicesByVendorId(UUID vendorId) {
        List<VendorService> vendorServices = vendorServiceRepository.findByVendorId(vendorId);
        return vendorServices.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    private VendorServiceResponseDto convertToResponseDto(VendorService vendorService) {
        VendorServiceResponseDto responseDto = new VendorServiceResponseDto();
        responseDto.setId(vendorService.getId());
        responseDto.setVendorName(vendorService.getVendor().getName());

        // Set the serviceType object
        VendorServiceResponseDto.ServiceTypeDto serviceTypeDto = new VendorServiceResponseDto.ServiceTypeDto();
        serviceTypeDto.setId(vendorService.getServiceType().getId());
        serviceTypeDto.setName(vendorService.getServiceType().getName());
        responseDto.setServiceType(serviceTypeDto);

        responseDto.setTdsRate(vendorService.getTdsRate());
        responseDto.setVendorContact(vendorService.getVendor().getContact());
        responseDto.setVendorGst(vendorService.getVendor().getGst());
        responseDto.setVendorTan(vendorService.getVendor().getTan());
        responseDto.setVendorCin(vendorService.getVendor().getCin());

        return responseDto;
    }
}