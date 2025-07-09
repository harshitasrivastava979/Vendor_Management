package com.vendormanagement.vendor_management_system.controller;

import com.vendormanagement.vendor_management_system.dto.VendorServiceRequestDto;
import com.vendormanagement.vendor_management_system.dto.VendorServiceResponseDto;
import com.vendormanagement.vendor_management_system.entity.VendorService;
import com.vendormanagement.vendor_management_system.mapper.VendorServiceMap;
import com.vendormanagement.vendor_management_system.service.VendorServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.vendormanagement.vendor_management_system.repository.VendorServiceRepository;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vendor-services")
public class VendorServiceController {

    @Autowired
    private VendorServiceService vendorServiceService;

    @Autowired
    private VendorServiceRepository vendorServiceRepository;

    @PostMapping
    public ResponseEntity<VendorServiceResponseDto> createVendorService(@RequestBody VendorServiceRequestDto requestDto) {
        return ResponseEntity.ok(vendorServiceService.createVendorService(requestDto));
    }

    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<VendorServiceResponseDto>> getVendorServicesByVendorId(@PathVariable UUID vendorId) {
        try {
            List<VendorServiceResponseDto> services = vendorServiceService.getVendorServicesByVendorId(vendorId);
            return ResponseEntity.ok(services);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendorServiceResponseDto> updateVendorService(@PathVariable UUID id,
                                                                        @RequestBody VendorServiceRequestDto requestDto) {
        try {
            VendorServiceResponseDto updated = vendorServiceService.updateVendorService(id, requestDto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVendorService(@PathVariable UUID id) {
        try {
            vendorServiceService.deleteVendorService(id);
            return ResponseEntity.ok("VendorService deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorServiceResponseDto> getVendorServiceById(@PathVariable UUID id) {
        try {
            VendorService vendorService = vendorServiceRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("VendorService not found"));
            VendorServiceResponseDto dto = VendorServiceMap.toDto(vendorService);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}