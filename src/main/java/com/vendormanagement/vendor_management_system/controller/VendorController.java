package com.vendormanagement.vendor_management_system.controller;

import com.vendormanagement.vendor_management_system.dto.VendorRequestDto;
import com.vendormanagement.vendor_management_system.entity.Vendor;
import com.vendormanagement.vendor_management_system.repository.VendorRepository;
import com.vendormanagement.vendor_management_system.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    @Qualifier("vendorImp")
    private VendorService vendorService;

    @GetMapping
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    @PostMapping
    public Vendor createVendor(@RequestBody VendorRequestDto dto) {
        return vendorService.createVendor(dto);
    }

    @GetMapping("/test")
    public String test() {
        return "Vendor Management System is working!";
    }
}
