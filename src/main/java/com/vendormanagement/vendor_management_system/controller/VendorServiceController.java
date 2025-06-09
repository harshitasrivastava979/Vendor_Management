package com.vendormanagement.vendor_management_system.controller;

import com.vendormanagement.vendor_management_system.dto.VendorServiceRequestDto;
import com.vendormanagement.vendor_management_system.dto.VendorServiceResponseDto;
import com.vendormanagement.vendor_management_system.entity.VendorService;
import com.vendormanagement.vendor_management_system.service.VendorServiceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vendor-services")
public class VendorServiceController {

    @Autowired
    private VendorServiceService vendorServiceService;

    @PostMapping
    public ResponseEntity<VendorServiceResponseDto> createVendorService(@RequestBody VendorServiceRequestDto requestDto) {
        return ResponseEntity.ok(vendorServiceService.createVendorService(requestDto));
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<VendorServiceResponseDto>> getVendorServicesByVendorId(@PathVariable UUID vendorId) {
        try {
            List<VendorServiceResponseDto> services = vendorServiceService.getVendorServicesByVendorId(vendorId);
            return ResponseEntity.ok(services);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}