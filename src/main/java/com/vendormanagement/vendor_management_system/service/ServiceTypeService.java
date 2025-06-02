package com.vendormanagement.vendor_management_system.service;

import com.vendormanagement.vendor_management_system.dto.ServiceTypeRequestDto;
import com.vendormanagement.vendor_management_system.entity.ServiceType;

public interface ServiceTypeService {


    ServiceType createServiceType(ServiceTypeRequestDto dto);
}