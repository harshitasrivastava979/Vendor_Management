package com.vendormanagement.vendor_management_system.service;

import com.vendormanagement.vendor_management_system.dto.ServiceTypeRequestDto;
import com.vendormanagement.vendor_management_system.entity.ServiceType;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public interface ServiceTypeService {


    ServiceType createServiceType(ServiceTypeRequestDto dto);

      List<ServiceType> getAllServiceTypes();

    ServiceType getServiceTypeById(UUID id);

    ServiceType updateServiceType(UUID id, ServiceTypeRequestDto dto);

    void deleteServiceType(UUID id);

}