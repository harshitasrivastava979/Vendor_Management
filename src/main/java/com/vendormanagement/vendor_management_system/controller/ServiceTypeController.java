package com.vendormanagement.vendor_management_system.controller;

import com.vendormanagement.vendor_management_system.dto.ServiceTypeRequestDto;
import com.vendormanagement.vendor_management_system.entity.ServiceType;
import com.vendormanagement.vendor_management_system.service.ServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/service-types")
public class ServiceTypeController {

    @Autowired
    private ServiceTypeService serviceTypeService;

    @PostMapping
    public ResponseEntity<ServiceType> createServiceType(@RequestBody ServiceTypeRequestDto dto) {
        ServiceType newServiceType = serviceTypeService.createServiceType(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newServiceType);
    }

    // Add other endpoints as needed
}