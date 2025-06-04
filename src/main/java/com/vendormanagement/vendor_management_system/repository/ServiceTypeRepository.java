package com.vendormanagement.vendor_management_system.repository;

import com.vendormanagement.vendor_management_system.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, UUID> {

        boolean existsByName(String name);

}
