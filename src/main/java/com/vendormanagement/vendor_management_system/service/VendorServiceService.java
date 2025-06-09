package com.vendormanagement.vendor_management_system.service;

import com.vendormanagement.vendor_management_system.dto.VendorServiceRequestDto;
import com.vendormanagement.vendor_management_system.dto.VendorServiceResponseDto;

import java.util.List;
import java.util.UUID;

public interface VendorServiceService {
    VendorServiceResponseDto createVendorService(VendorServiceRequestDto requestDto);

    List<VendorServiceResponseDto> getVendorServicesByVendorId(UUID vendorId);

 //   List<VendorServiceResponseDto> getAllVendorServices();

   // VendorServiceResponseDto getVendorServiceById(UUID id);

    VendorServiceResponseDto updateVendorService(UUID id, VendorServiceRequestDto requestDto);

    void deleteVendorService(UUID id);

   // List<VendorServiceResponseDto> getVendorServicesByVendorId(Long vendorId);
}