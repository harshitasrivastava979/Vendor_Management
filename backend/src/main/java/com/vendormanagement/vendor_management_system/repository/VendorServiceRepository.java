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

import com.vendormanagement.vendor_management_system.entity.ServiceType;
import com.vendormanagement.vendor_management_system.entity.Vendor;
import com.vendormanagement.vendor_management_system.entity.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class VendorServiceRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    private final RowMapper<VendorService> vendorServiceRowMapper = new RowMapper<VendorService>() {
        @Override
        public VendorService mapRow(ResultSet rs, int rowNum) throws SQLException {
            VendorService vendorService = new VendorService();
            vendorService.setId(UUID.fromString(rs.getString("id")));
            
            // Load vendor
            UUID vendorId = UUID.fromString(rs.getString("vendor_id"));
            Optional<Vendor> vendor = vendorRepository.findById(vendorId);
            vendorService.setVendor(vendor.orElse(null));
            
            // Load service type
            UUID serviceTypeId = UUID.fromString(rs.getString("service_id"));
            Optional<ServiceType> serviceType = serviceTypeRepository.findById(serviceTypeId);
            vendorService.setServiceType(serviceType.orElse(null));
            
            vendorService.setTdsRate(rs.getBigDecimal("tds_rate"));
            vendorService.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            vendorService.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            
            // Check if used in invoices
            boolean usedInInvoices = isUsedInInvoices(vendorService.getId());
            vendorService.setUsedInInvoices(usedInInvoices);
            
            return vendorService;
        }
    };

    public List<VendorService> findAll() {
        String sql = "SELECT * FROM vendor_services ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, vendorServiceRowMapper);
    }

    public Optional<VendorService> findById(UUID id) {
        String sql = "SELECT * FROM vendor_services WHERE id = ?";
        List<VendorService> results = jdbcTemplate.query(sql, vendorServiceRowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<VendorService> findByVendorId(UUID vendorId) {
        String sql = "SELECT * FROM vendor_services WHERE vendor_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, vendorServiceRowMapper, vendorId);
    }

    public VendorService save(VendorService vendorService) {
        if (vendorService.getId() == null) {
            // Insert new vendor service
            vendorService.setId(UUID.randomUUID());
            String sql = "INSERT INTO vendor_services (id, vendor_id, service_id, tds_rate, created_at, updated_at) VALUES (?, ?, ?, ?, NOW(), NOW())";
            jdbcTemplate.update(sql,
                vendorService.getId(),
                vendorService.getVendor().getId(),
                vendorService.getServiceType().getId(),
                vendorService.getTdsRate()
            );
        } else {
            // Update existing vendor service
            String sql = "UPDATE vendor_services SET vendor_id = ?, service_id = ?, tds_rate = ?, updated_at = NOW() WHERE id = ?";
            jdbcTemplate.update(sql,
                vendorService.getVendor().getId(),
                vendorService.getServiceType().getId(),
                vendorService.getTdsRate(),
                vendorService.getId()
            );
        }
        return vendorService;
    }

    public void delete(VendorService vendorService) {
        String sql = "DELETE FROM vendor_services WHERE id = ?";
        jdbcTemplate.update(sql, vendorService.getId());
    }

    public void deleteById(UUID id) {
        String sql = "DELETE FROM vendor_services WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean isUsedInInvoices(UUID vendorServiceId) {
        String sql = "SELECT COUNT(*) FROM invoices WHERE vendorservice_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, vendorServiceId);
        return count != null && count > 0;
    }

    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM vendor_services WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public List<VendorService> findByVendorIdAndServiceTypeId(UUID vendorId, UUID serviceTypeId) {
        String sql = "SELECT * FROM vendor_services WHERE vendor_id = ? AND service_id = ?";
        return jdbcTemplate.query(sql, vendorServiceRowMapper, vendorId, serviceTypeId);
    }
}