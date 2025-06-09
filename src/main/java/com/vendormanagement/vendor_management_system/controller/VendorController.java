package com.vendormanagement.vendor_management_system.controller;

import com.vendormanagement.vendor_management_system.dto.VendorRequestDto;
import com.vendormanagement.vendor_management_system.dto.VendorServiceRequestDto;
import com.vendormanagement.vendor_management_system.entity.Vendor;
import com.vendormanagement.vendor_management_system.entity.VendorService;
import com.vendormanagement.vendor_management_system.repository.VendorRepository;
import com.vendormanagement.vendor_management_system.repository.VendorServiceRepository;
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
    private VendorServiceHandler vendorServiceHandler;
    @Autowired
    private VendorServiceRepository vendorServiceRepository ;

    @GetMapping
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }
    @GetMapping("/{id}")
    public Vendor getVendorById(@PathVariable UUID id){
        return vendorServiceHandler.getVendorbyId( id);


    }

    @PostMapping
    public Vendor createVendor(@RequestBody VendorRequestDto dto) {
        return vendorServiceHandler.createVendor(dto);
    }
   @DeleteMapping("/{id}")
   public ResponseEntity<String> deleteVendor(@PathVariable UUID id){

         vendorServiceHandler.deleteVendor(id) ;
         return ResponseEntity.ok("Vendor Delete Successfully");
   }
    @GetMapping("/vendor-services")
    public ResponseEntity<List<VendorServiceRequestDto>> getVendorServices() {
        List<VendorServiceRequestDto> result = vendorServiceHandler.getVendorsWithServicesAssignment();
        return ResponseEntity.ok(result);
    }


    @GetMapping("/test")
    public String test() {
        return "Vendor Management System is working!";
    }
}
