package com.vendormanagement.vendor_management_system.service.implementation;

import com.vendormanagement.vendor_management_system.dto.ServiceTypeRequestDto;
import com.vendormanagement.vendor_management_system.entity.ServiceType;
import com.vendormanagement.vendor_management_system.mapper.ServiceTypeMap;
import com.vendormanagement.vendor_management_system.repository.ServiceTypeRepository;
import com.vendormanagement.vendor_management_system.service.ServiceTypeService;
import org.springframework.stereotype.Service;
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
    }

