package com.vendormanagement.vendor_management_system.controller;

import com.vendormanagement.vendor_management_system.dto.VendorRequestDto;
import com.vendormanagement.vendor_management_system.entity.Vendor;
import com.vendormanagement.vendor_management_system.repository.VendorRepository;
import com.vendormanagement.vendor_management_system.service.VendorServiceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    @Qualifier("vendorImp")
    private VendorServiceHandler vendorService;

    @GetMapping
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }
    @GetMapping("/{id}")
    public Vendor getVendorById(@PathVariable UUID id){
        return vendorService.getVendorbyId( id);


    }

    @PostMapping
    public Vendor createVendor(@RequestBody VendorRequestDto dto) {
        return vendorService.createVendor(dto);
    }
   @DeleteMapping("/{id}")
   public ResponseEntity<String> deleteVendor(@PathVariable UUID id){

         vendorService.deleteVendor(id) ;
         return ResponseEntity.ok("Vendor Delete Successfully");
   }


    @GetMapping("/test")
    public String test() {
        return "Vendor Management System is working!";
    }
}
