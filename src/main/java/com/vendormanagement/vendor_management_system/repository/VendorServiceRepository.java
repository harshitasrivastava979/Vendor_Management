//package com.vendormanagement.vendor_management_system.repository;
//
//import com.vendormanagement.vendor_management_system.entity.VendorService;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.UUID;
//
//@Repository
//public interface VendorServiceRepository extends JpaRepository<VendorService, UUID> {
//    List<VendorService> findByVendorId(UUID vendorId);
//    List<VendorService> findByServiceTypeId(UUID serviceTypeId);
//    boolean existsByVendorIdAndServiceTypeId(UUID vendorId, UUID serviceTypeId);
//    //boolean existsByVendorIdAndServiceTypeId(UUID vendorId, UUID serviceTypeId);
//}
package com.vendormanagement.vendor_management_system.repository;

import com.vendormanagement.vendor_management_system.entity.VendorService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VendorServiceRepository extends JpaRepository<VendorService, UUID> {

    List<VendorService> findByVendorId(UUID vendorId);

    List<VendorService> findByServiceTypeId(UUID serviceTypeId);

    // ❌ This might not work correctly - serviceType is the field name, but service_id is the column name
    boolean existsByVendorIdAndServiceTypeId(UUID vendorId, UUID serviceTypeId);

    // ✅ Try this alternative - using the entity field path
    @Query("SELECT COUNT(vs) > 0 FROM VendorService vs WHERE vs.vendor.id = :vendorId AND vs.serviceType.id = :serviceTypeId")
    boolean existsByVendorIdAndServiceType_Id(UUID vendorId, UUID serviceTypeId);

    // ✅ Or use a custom query to be explicit
    @Query("SELECT COUNT(vs) > 0 FROM VendorService vs WHERE vs.vendor.id = :vendorId AND vs.serviceType.id = :serviceTypeId")
    boolean existsByVendorAndServiceType(@Param("vendorId") UUID vendorId, @Param("serviceTypeId") UUID serviceTypeId);

    // ✅ Find method for find-or-create pattern
    Optional<VendorService> findByVendorIdAndServiceType_Id(UUID vendorId, UUID serviceTypeId);
}