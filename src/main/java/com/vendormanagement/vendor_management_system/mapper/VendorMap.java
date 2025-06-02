package com.vendormanagement.vendor_management_system.mapper;

import com.vendormanagement.vendor_management_system.dto.VendorRequestDto;
import com.vendormanagement.vendor_management_system.entity.Vendor;

public class VendorMap {

    public static Vendor mapToEntity(VendorRequestDto dto) {
        Vendor vendor = new Vendor();
        vendor.setName(dto.getName());
        vendor.setAddress(dto.getAddress());
        vendor.setContact(dto.getContact());
        vendor.setGstTanCin(dto.getGstTanCin());
        vendor.setBankAcc(dto.getBankAcc());
        vendor.setIfsc(dto.getIfsc());
        vendor.setNeftEnabled(dto.getNeftEnabled());
        return vendor;
    }

    public VendorRequestDto mapVendorToDto(Vendor vendor) {
        VendorRequestDto dto = new VendorRequestDto();
        dto.setName(vendor.getName());
        dto.setAddress(vendor.getAddress());
        dto.setContact(vendor.getContact());
        dto.setGstTanCin(vendor.getGstTanCin());
        dto.setBankAcc(vendor.getBankAcc());
        dto.setIfsc(vendor.getIfsc());
        dto.setNeftEnabled(vendor.getNeftEnabled());
        return dto;
    }
}
