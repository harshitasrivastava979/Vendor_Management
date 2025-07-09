package com.vendormanagement.vendor_management_system.repository;

import com.vendormanagement.vendor_management_system.entity.ServiceType;
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
public class ServiceTypeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<ServiceType> serviceTypeRowMapper = new RowMapper<ServiceType>() {
        @Override
        public ServiceType mapRow(ResultSet rs, int rowNum) throws SQLException {
            ServiceType serviceType = new ServiceType();
            serviceType.setId(UUID.fromString(rs.getString("id")));
            serviceType.setName(rs.getString("name"));
            serviceType.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            serviceType.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return serviceType;
        }
    };

    public List<ServiceType> findAll() {
        String sql = "SELECT * FROM service_types ORDER BY name";
        return jdbcTemplate.query(sql, serviceTypeRowMapper);
    }

    public Optional<ServiceType> findById(UUID id) {
        String sql = "SELECT * FROM service_types WHERE id = ?";
        List<ServiceType> results = jdbcTemplate.query(sql, serviceTypeRowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public ServiceType save(ServiceType serviceType) {
        if (serviceType.getId() == null) {
            // Insert new service type
            serviceType.setId(UUID.randomUUID());
            String sql = "INSERT INTO service_types (id, name, created_at, updated_at) VALUES (?, ?, NOW(), NOW())";
            jdbcTemplate.update(sql,
                serviceType.getId(),
                serviceType.getName()
            );
        } else {
            // Update existing service type
            String sql = "UPDATE service_types SET name = ?, updated_at = NOW() WHERE id = ?";
            jdbcTemplate.update(sql,
                serviceType.getName(),
                serviceType.getId()
            );
        }
        return serviceType;
    }

    public void deleteById(UUID id) {
        String sql = "DELETE FROM service_types WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM service_types WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM service_types WHERE LOWER(name) = LOWER(?)";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count != null && count > 0;
    }
}
