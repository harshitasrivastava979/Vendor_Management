package com.vendormanagement.vendor_management_system.repository;
import  com.vendormanagement.vendor_management_system.entity.Vendor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, UUID> {

    List<Vendor> findByNameContainingIgnoreCase(String name);

    boolean existsByName(String name);

}