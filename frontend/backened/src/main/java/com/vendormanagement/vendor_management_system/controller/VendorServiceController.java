package com.vendormanagement.vendor_management_system.controller;

import com.vendormanagement.vendor_management_system.dto.VendorServiceRequestDto;
import com.vendormanagement.vendor_management_system.entity.VendorService;
import com.vendormanagement.vendor_management_system.service.VendorServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendor-services")
public class VendorServiceController {

    @Autowired
    private VendorServiceService vendorServiceService;

    @PostMapping
    public ResponseEntity<VendorService> createVendorService(
            @RequestBody VendorServiceRequestDto dto) {
        VendorService newVendorService = vendorServiceService.createVendorService(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newVendorService);
    }
}