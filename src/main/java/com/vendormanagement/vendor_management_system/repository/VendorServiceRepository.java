package com.vendormanagement.vendor_management_system.repository;

import com.vendormanagement.vendor_management_system.entity.VendorService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VendorServiceRepository extends JpaRepository<VendorService, UUID> {
    boolean existsByVendorIdAndServiceTypeId(UUID vendorId, UUID serviceTypeId);
}