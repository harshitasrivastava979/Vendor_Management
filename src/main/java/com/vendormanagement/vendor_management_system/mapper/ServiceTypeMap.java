package com.vendormanagement.vendor_management_system.mapper;


import com.vendormanagement.vendor_management_system.dto.ServiceTypeRequestDto;
import com.vendormanagement.vendor_management_system.entity.ServiceType;

public class ServiceTypeMap {

        public static ServiceType mapToEntity(ServiceTypeRequestDto dto) {
            ServiceType serviceType = new ServiceType();
            serviceType.setName(dto.getName());
            return serviceType;
        }
    }

