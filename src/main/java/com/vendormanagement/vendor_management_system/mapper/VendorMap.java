package com.vendormanagement.vendor_management_system.mapper;

import com.vendormanagement.vendor_management_system.dto.VendorRequestDto;
import com.vendormanagement.vendor_management_system.dto.VendorResponseDto;
import com.vendormanagement.vendor_management_system.dto.VendorServiceResponseDto;
import com.vendormanagement.vendor_management_system.entity.Vendor;
import com.vendormanagement.vendor_management_system.entity.VendorService;

import java.util.List;
import java.util.stream.Collectors;

public class VendorMap {

    public static Vendor mapToEntity(VendorRequestDto dto) {
        Vendor vendor = new Vendor();
        vendor.setName(dto.getName());
        vendor.setAddress(dto.getAddress());
        vendor.setContact(dto.getContact());
        vendor.setGst(dto.getGst());
        vendor.setTan(dto.getTan());
        vendor.setCin(dto.getCin());
        vendor.setBankAcc(dto.getBankAcc());
        vendor.setBankAccType(dto.getBankAccType());
        vendor.setBranchAdd(dto.getBranchAdd());
        vendor.setBankName(dto.getBankName());
        vendor.setIfsc(dto.getIfsc());
        vendor.setNeftEnabled(dto.getNeftEnabled());
        vendor.setBeneficiaryCode(dto.getBeneficiaryCode());
        return vendor;
    }

    public VendorRequestDto mapVendorToDto(Vendor vendor) {
        VendorRequestDto dto = new VendorRequestDto();
        dto.setName(vendor.getName());
        dto.setAddress(vendor.getAddress());
        dto.setContact(vendor.getContact());
        dto.setGst(vendor.getGst());
        dto.setTan(vendor.getTan());
        dto.setCin(vendor.getCin());
        dto.setBankAcc(vendor.getBankAcc());
        dto.setBankAccType(vendor.getBankAccType());
        dto.setBankName(vendor.getBankName());
        dto.setBranchAdd(vendor.getBranchAdd());
        dto.setIfsc(vendor.getIfsc());
        dto.setNeftEnabled(vendor.getNeftEnabled());
        dto.setBeneficiaryCode(vendor.getBeneficiaryCode());
        return dto;
    }

    public static VendorResponseDto toResponseDto(Vendor vendor) {
        VendorResponseDto dto = new VendorResponseDto();
        
        dto.setId(vendor.getId());
        dto.setName(vendor.getName());
        dto.setAddress(vendor.getAddress());
        dto.setContact(vendor.getContact());
        dto.setGst(vendor.getGst());
        dto.setTan(vendor.getTan());
        dto.setCin(vendor.getCin());
        dto.setBankAcc(vendor.getBankAcc());
        dto.setBankAccType(vendor.getBankAccType());
        dto.setBankName(vendor.getBankName());
        dto.setBranchAdd(vendor.getBranchAdd());
        dto.setIfsc(vendor.getIfsc());
        dto.setNeftEnabled(vendor.getNeftEnabled());
        dto.setBeneficiaryCode(vendor.getBeneficiaryCode());
        dto.setCreatedAt(vendor.getCreatedAt());
        dto.setUpdatedAt(vendor.getUpdatedAt());
        
        // Map vendor services with usedInInvoices information
        if (vendor.getVendorServices() != null) {
            List<VendorServiceResponseDto> vendorServiceDtos = vendor.getVendorServices().stream()
                    .map(VendorServiceMap::toDto)
                    .collect(Collectors.toList());
            dto.setVendorServiceList(vendorServiceDtos);
        }
        
        return dto;
    }
}
