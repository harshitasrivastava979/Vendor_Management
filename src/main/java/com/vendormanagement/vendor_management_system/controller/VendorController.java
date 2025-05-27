package com.vendormanagement.vendor_management_system.controller;
import com.vendormanagement.vendor_management_system.entity.Vendor;
import com.vendormanagement.vendor_management_system.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    @Autowired
    private VendorRepository vendorRepository;

    @GetMapping()
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    @PostMapping()
    public Vendor createVendor(@RequestBody Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    @GetMapping("/test")
    public String test() {
        return "Vendor Management System is working!";
    }
}