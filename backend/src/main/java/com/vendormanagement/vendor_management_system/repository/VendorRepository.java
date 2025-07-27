package com.vendormanagement.vendor_management_system.repository;

import com.vendormanagement.vendor_management_system.entity.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class VendorRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Vendor> vendorRowMapper = new RowMapper<Vendor>() {
        @Override
        public Vendor mapRow(ResultSet rs, int rowNum) throws SQLException {
            Vendor vendor = new Vendor();
            vendor.setId(UUID.fromString(rs.getString("id")));
            vendor.setName(rs.getString("name"));
            vendor.setAddress(rs.getString("address"));
            vendor.setContact(rs.getString("contact"));
            vendor.setGst(rs.getString("gst"));
            vendor.setTan(rs.getString("tan"));
            vendor.setCin(rs.getString("cin"));
            vendor.setBankAcc(rs.getString("bank_acc"));
            vendor.setBankAccType(rs.getString("bank_acc_type"));
            vendor.setBankName(rs.getString("bank_name"));
            vendor.setBranchAdd(rs.getString("branch_add"));
            vendor.setIfsc(rs.getString("ifsc"));
            vendor.setNeftEnabled(rs.getBoolean("neft_enabled"));
            vendor.setBeneficiaryCode(rs.getString("beneficiary_code"));
            vendor.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            vendor.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return vendor;
        }
    };

    public List<Vendor> findAll() {
        String sql = "SELECT * FROM vendors ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, vendorRowMapper);
    }

    public Optional<Vendor> findById(UUID id) {
        String sql = "SELECT * FROM vendors WHERE id = ?";
        List<Vendor> results = jdbcTemplate.query(sql, vendorRowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Vendor save(Vendor vendor) {
        if (vendor.getId() == null) {
            // Insert new vendor
            vendor.setId(UUID.randomUUID());
            String sql = """
                INSERT INTO vendors (id, name, address, contact, gst, tan, cin, bank_acc, 
                bank_acc_type, bank_name, branch_add, ifsc, neft_enabled, beneficiary_code, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
                """;
            jdbcTemplate.update(sql,
                vendor.getId(),
                vendor.getName(),
                vendor.getAddress(),
                vendor.getContact(),
                vendor.getGst(),
                vendor.getTan(),
                vendor.getCin(),
                vendor.getBankAcc(),
                vendor.getBankAccType(),
                vendor.getBankName(),
                vendor.getBranchAdd(),
                vendor.getIfsc(),
                vendor.getNeftEnabled(),
                vendor.getBeneficiaryCode()
            );
        } else {
            // Update existing vendor
            String sql = """
                UPDATE vendors SET name = ?, address = ?, contact = ?, gst = ?, tan = ?, cin = ?, 
                bank_acc = ?, bank_acc_type = ?, bank_name = ?, branch_add = ?, ifsc = ?, 
                neft_enabled = ?, beneficiary_code = ?, updated_at = NOW()
                WHERE id = ?
                """;
            jdbcTemplate.update(sql,
                vendor.getName(),
                vendor.getAddress(),
                vendor.getContact(),
                vendor.getGst(),
                vendor.getTan(),
                vendor.getCin(),
                vendor.getBankAcc(),
                vendor.getBankAccType(),
                vendor.getBankName(),
                vendor.getBranchAdd(),
                vendor.getIfsc(),
                vendor.getNeftEnabled(),
                vendor.getBeneficiaryCode(),
                vendor.getId()
            );
        }
        return vendor;
    }

    public void deleteById(UUID id) {
        String sql = "DELETE FROM vendors WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM vendors WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public List<Vendor> findByNameContainingIgnoreCase(String name) {
        String sql = "SELECT * FROM vendors WHERE LOWER(name) LIKE LOWER(?) ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, vendorRowMapper, "%" + name + "%");
    }

    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM vendors WHERE LOWER(name) = LOWER(?)";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }
}