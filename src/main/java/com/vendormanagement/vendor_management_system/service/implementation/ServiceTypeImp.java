package com.vendormanagement.vendor_management_system.service.implementation;

import com.vendormanagement.vendor_management_system.dto.ServiceTypeRequestDto;
import com.vendormanagement.vendor_management_system.entity.ServiceType;
import com.vendormanagement.vendor_management_system.mapper.ServiceTypeMap;
import com.vendormanagement.vendor_management_system.repository.ServiceTypeRepository;
import com.vendormanagement.vendor_management_system.service.ServiceTypeService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
    public class ServiceTypeImp implements ServiceTypeService {

        private final ServiceTypeRepository serviceTypeRepository;

        public ServiceTypeImp(ServiceTypeRepository serviceTypeRepository) {
            this.serviceTypeRepository = serviceTypeRepository;
        }

        @Override
        public ServiceType createServiceType(ServiceTypeRequestDto dto) {
            // Add uniqueness check
            if (serviceTypeRepository.existsByName(dto.getName())) {
                throw new RuntimeException("Service type already exists");
            }

            ServiceType serviceType = ServiceTypeMap.mapToEntity(dto);
            return serviceTypeRepository.save(serviceType);
        }
    @Override
    public List<ServiceType> getAllServiceTypes() {
        return serviceTypeRepository.findAll();
    }
    @Override
    public ServiceType getServiceTypeById(UUID id) {
        return serviceTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ServiceType not found with id: " + id));
    }

    @Override
    public ServiceType updateServiceType(UUID id, ServiceTypeRequestDto dto) {
        ServiceType existing = getServiceTypeById(id);
        existing.setName(dto.getName());
        return serviceTypeRepository.save(existing);
    }

    @Override
    public void deleteServiceType(UUID id) {
        ServiceType existing = getServiceTypeById(id);
        serviceTypeRepository.delete(existing);
    }
    




}

