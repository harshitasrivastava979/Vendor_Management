package com.vendormanagement.vendor_management_system.service.implementation;

import com.vendormanagement.vendor_management_system.dto.VendorRequestDto;
import com.vendormanagement.vendor_management_system.dto.VendorServiceRequestDto;
import com.vendormanagement.vendor_management_system.entity.ServiceType;
import com.vendormanagement.vendor_management_system.entity.Vendor;
import com.vendormanagement.vendor_management_system.entity.VendorService;
import com.vendormanagement.vendor_management_system.repository.ServiceTypeRepository;
import com.vendormanagement.vendor_management_system.repository.VendorRepository;
import com.vendormanagement.vendor_management_system.repository.VendorServiceRepository;
import com.vendormanagement.vendor_management_system.service.VendorServiceHandler;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VendorImp implements VendorServiceHandler {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private VendorServiceRepository vendorServiceRepository;

    @Override
  //  @Transactional
    public Vendor createVendor(VendorRequestDto dto) {
        // Create and populate the vendor from DTO basic fields
        Vendor vendor = new Vendor();
        vendor.setName(dto.getName());
        vendor.setAddress(dto.getAddress());
        vendor.setContact(dto.getContact());
        vendor.setGstTanCin(dto.getGstTanCin());
        vendor.setBankAcc(dto.getBankAcc());
        vendor.setIfsc(dto.getIfsc());
        vendor.setNeftEnabled(dto.getNeftEnabled());

        // Save vendor first to generate an ID
        Vendor savedVendor = vendorRepository.save(vendor);

//         Process VendorServices if provided in the DTO
        if (dto.getVendorServiceList() != null && !dto.getVendorServiceList().isEmpty()) {
            for (VendorServiceRequestDto serviceDto : dto.getVendorServiceList()) {
                // Retrieve the ServiceType; throw an error if not found
                ServiceType serviceType = serviceTypeRepository.findById(serviceDto.getServiceTypeId())
                        .orElseThrow(() -> new EntityNotFoundException("Service Type not found with ID: " + serviceDto.getServiceTypeId()));

                VendorService vendorService = new VendorService( savedVendor,  serviceType, serviceDto.getTdsRate() );

                // Link the service type to the vendor via helper method.
//                savedVendor.addService(serviceType, serviceDto.getTdsRate());
                vendorServiceRepository.save(vendorService);
            }
            // Save the vendor again to ensure the vendor services are persisted

//            savedVendor = vendorRepository.save(savedVendor);
        }

        return savedVendor;
    }

    @Override
    public Vendor getVendorbyId(UUID id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vendor not found with ID: " + id));
    }

    @Override
    public void deleteVendor(UUID id) {
        // Validate existence before attempting deletion
        if (!vendorRepository.existsById(id)) {
            throw new EntityNotFoundException("Vendor not found with ID: " + id);
        }
        vendorRepository.deleteById(id);
    }

    @Override
    public List<VendorServiceRequestDto> getVendorsWithServicesAssignment() {
        List<VendorService> vendorServices = vendorServiceRepository.findAll();
        return vendorServices.stream()
                .map(vs -> {
                    VendorServiceRequestDto dto = new VendorServiceRequestDto();
                    dto.setVendorId(vs.getVendor().getId());
                    dto.setServiceTypeId(vs.getServiceType().getId());
                    dto.setTdsRate(vs.getTdsRate());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
