package com.vendormanagement.vendor_management_system.service.implementation;

import com.vendormanagement.vendor_management_system.dto.VendorServiceRequestDto;
import com.vendormanagement.vendor_management_system.dto.VendorServiceResponseDto;
import com.vendormanagement.vendor_management_system.entity.ServiceType;
import com.vendormanagement.vendor_management_system.entity.Vendor;
import com.vendormanagement.vendor_management_system.entity.VendorService;
import com.vendormanagement.vendor_management_system.repository.ServiceTypeRepository;
import com.vendormanagement.vendor_management_system.repository.VendorRepository;
import com.vendormanagement.vendor_management_system.repository.VendorServiceRepository;
import com.vendormanagement.vendor_management_system.service.VendorServiceService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    @Override
    @Transactional
    public VendorServiceResponseDto createVendorService(VendorServiceRequestDto requestDto) {
        try {
            Vendor vendor = vendorRepository.findById(requestDto.getVendorId())
                    .orElseThrow(() -> new EntityNotFoundException("Vendor not found"));

            ServiceType serviceType = serviceTypeRepository.findById(requestDto.getServiceTypeId())
                    .orElseThrow(() -> new EntityNotFoundException("Service Type not found"));

            // ✅ Use the corrected method name
            boolean exists = vendorServiceRepository.existsByVendorIdAndServiceType_Id(
                    requestDto.getVendorId(), requestDto.getServiceTypeId());

            if (exists) {
                throw new IllegalArgumentException("Vendor is already assigned to this service type.");
            }

            VendorService vendorService = new VendorService();
            vendorService.setVendor(vendor);
            vendorService.setServiceType(serviceType);
            vendorService.setTdsRate(requestDto.getTdsRate());

            VendorService savedVendorService = vendorServiceRepository.save(vendorService);
            return convertToResponseDto(savedVendorService);

        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // ✅ Handle database constraint violation as backup
            if (e.getMessage().toLowerCase().contains("unique constraint") ||
                    e.getMessage().contains("vendor_services_vendor_id_service_id_key")) {
                throw new IllegalArgumentException("Vendor is already assigned to this service type.");
            }
            throw e;
        } catch (org.hibernate.exception.ConstraintViolationException e) {
            // ✅ Handle Hibernate constraint violation
            if (e.getConstraintName() != null && e.getConstraintName().contains("vendor_id_service_id")) {
                throw new IllegalArgumentException("Vendor is already assigned to this service type.");
            }
            throw e;
        }
    }



    @Override
    public VendorServiceResponseDto updateVendorService(UUID id, VendorServiceRequestDto requestDto) {
        return null;
    }

    @Override
    public void deleteVendorService(UUID id) {

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
        responseDto.setServiceTypeName(vendorService.getServiceType().getName());
        responseDto.setTdsRate(vendorService.getTdsRate());
        responseDto.setVendorContact(vendorService.getVendor().getContact());
        responseDto.setVendorGstTanCin(vendorService.getVendor().getGstTanCin());
        return responseDto;
    }
}