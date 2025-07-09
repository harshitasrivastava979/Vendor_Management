package com.vendormanagement.vendor_management_system.service.implementation;

import com.vendormanagement.vendor_management_system.dto.VendorRequestDto;
import com.vendormanagement.vendor_management_system.dto.VendorServiceRequestDto;
import com.vendormanagement.vendor_management_system.dto.VendorServiceResponseDto;
import com.vendormanagement.vendor_management_system.entity.ServiceType;
import com.vendormanagement.vendor_management_system.entity.Vendor;
import com.vendormanagement.vendor_management_system.entity.VendorService;
import com.vendormanagement.vendor_management_system.repository.ServiceTypeRepository;
import com.vendormanagement.vendor_management_system.repository.VendorRepository;
import com.vendormanagement.vendor_management_system.repository.VendorServiceRepository;
import com.vendormanagement.vendor_management_system.service.VendorServiceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.HashSet;

@Service
public class VendorImp implements VendorServiceHandler {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @Autowired
    private VendorServiceRepository vendorServiceRepository;

    @Override
    public Vendor createVendor(VendorRequestDto dto) {
        // Create and populate the vendor from DTO basic fields
        Vendor vendor = new Vendor();
        vendor.setName(dto.getName());
        vendor.setAddress(dto.getAddress());
        vendor.setContact(dto.getContact());
        vendor.setGst(dto.getGst());
        vendor.setTan(dto.getTan());
        vendor.setCin(dto.getCin());
        vendor.setBankAcc(dto.getBankAcc());
        vendor.setBankAccType(dto.getBankAccType());
        vendor.setBranchAdd(dto.getBranchAdd());
        vendor.setBankName(dto.getBankName());
        vendor.setIfsc(dto.getIfsc());
        vendor.setNeftEnabled(dto.getNeftEnabled());
        vendor.setBeneficiaryCode(dto.getBeneficiaryCode());

        // Save vendor first to generate an ID
        Vendor savedVendor = vendorRepository.save(vendor);

        // Process VendorServices if provided in the DTO
        if (dto.getVendorServiceList() != null && !dto.getVendorServiceList().isEmpty()) {
            for (VendorServiceRequestDto serviceDto : dto.getVendorServiceList()) {
                // Validate serviceTypeId is not null
                if (serviceDto.getServiceTypeId() == null) {
                    throw new IllegalArgumentException("Service Type ID cannot be null");
                }
                
                // Retrieve the ServiceType; throw an error if not found
                ServiceType serviceType = serviceTypeRepository.findById(serviceDto.getServiceTypeId())
                        .orElseThrow(() -> new RuntimeException("Service Type not found with ID: " + serviceDto.getServiceTypeId()));

                VendorService vendorService = new VendorService(savedVendor, serviceType, serviceDto.getTdsRate());
                vendorServiceRepository.save(vendorService);
            }
        }

        return savedVendor;
    }

    @Override
    public Vendor getVendorbyId(UUID id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found with ID: " + id));
        
        // Load vendor services to ensure they are available
        List<VendorService> vendorServices = vendorServiceRepository.findByVendorId(id);
        vendor.setVendorServices(new HashSet<>(vendorServices));
        
        return vendor;
    }

    @Override
    public void deleteVendor(UUID id) {
        // Validate existence before attempting deletion
        if (!vendorRepository.existsById(id)) {
            throw new RuntimeException("Vendor not found with ID: " + id);
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

    @Override
    public Vendor updateVendor(UUID id, VendorRequestDto dto) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor not found"));

        // Update main info
        vendor.setName(dto.getName());
        vendor.setAddress(dto.getAddress());
        vendor.setContact(dto.getContact());
        vendor.setGst(dto.getGst());
        vendor.setTan(dto.getTan());
        vendor.setCin(dto.getCin());
        vendor.setBankAcc(dto.getBankAcc());
        vendor.setBranchAdd(dto.getBranchAdd());
        vendor.setBankName(dto.getBankName());
        vendor.setIfsc(dto.getIfsc());
        vendor.setNeftEnabled(dto.getNeftEnabled());
        vendor.setBeneficiaryCode(dto.getBeneficiaryCode());

        // Save the updated vendor
        vendor = vendorRepository.save(vendor);

        // Get existing vendor services
        List<VendorService> existingServices = vendorServiceRepository.findByVendorId(id);
        
        // Process new vendor services from the payload
        if (dto.getVendorServiceList() != null) {
            for (VendorServiceRequestDto serviceDto : dto.getVendorServiceList()) {
                // Validate serviceTypeId is not null
                if (serviceDto.getServiceTypeId() == null) {
                    throw new IllegalArgumentException("Service Type ID cannot be null");
                }
                
                ServiceType serviceType = serviceTypeRepository.findById(serviceDto.getServiceTypeId())
                        .orElseThrow(() -> new RuntimeException("Service Type not found with ID: " + serviceDto.getServiceTypeId()));
                
                // Check if this service already exists
                VendorService existingService = existingServices.stream()
                        .filter(vs -> vs.getServiceType().getId().equals(serviceDto.getServiceTypeId()))
                        .findFirst()
                        .orElse(null);
                
                if (existingService != null) {
                    // Service exists - check if it's used in invoices
                    boolean usedInInvoices = vendorServiceRepository.isUsedInInvoices(existingService.getId());
                    
                    if (usedInInvoices) {
                        // Service is used in invoices - only update TDS rate
                        // Verify that the service type hasn't changed
                        if (!existingService.getServiceType().getId().equals(serviceDto.getServiceTypeId())) {
                            throw new RuntimeException("Cannot change service type for service used in invoices. Service ID: " + existingService.getId());
                        }
                        existingService.setTdsRate(serviceDto.getTdsRate());
                        vendorServiceRepository.save(existingService);
                    } else {
                        // Service not used in invoices - can update everything
                        existingService.setTdsRate(serviceDto.getTdsRate());
                        vendorServiceRepository.save(existingService);
                    }
                    
                    // Remove from existing services list to mark as processed
                    existingServices.remove(existingService);
                } else {
                    // New service - create it
                    VendorService newVendorService = new VendorService(vendor, serviceType, serviceDto.getTdsRate());
                    vendorServiceRepository.save(newVendorService);
                }
            }
            
            // Remove any remaining existing services that weren't in the new payload
            // Only remove if they're not used in invoices
            for (VendorService remainingService : existingServices) {
                boolean usedInInvoices = vendorServiceRepository.isUsedInInvoices(remainingService.getId());
                if (!usedInInvoices) {
                    vendorServiceRepository.delete(remainingService);
                } else {
                    // Log that we're preserving a service used in invoices
                    System.out.println("Preserving service " + remainingService.getId() + " as it's used in invoices");
                }
            }
        }

        return vendor;
    }
}