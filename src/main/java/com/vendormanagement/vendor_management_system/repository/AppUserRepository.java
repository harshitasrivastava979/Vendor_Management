package com.vendormanagement.vendor_management_system.repository;

import com.vendormanagement.vendor_management_system.entity.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class AppUserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<AppUser> appUserRowMapper = new RowMapper<AppUser>() {
        @Override
        public AppUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            AppUser appUser = new AppUser();
            appUser.setUsername(rs.getString("username"));
            appUser.setPassword(rs.getString("password"));
            return appUser;
        }
    };

    public List<AppUser> findAll() {
        String sql = "SELECT * FROM users ORDER BY id DESC";
        return jdbcTemplate.query(sql, appUserRowMapper);
    }

    public Optional<AppUser> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        List<AppUser> results = jdbcTemplate.query(sql, appUserRowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Optional<AppUser> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        List<AppUser> results = jdbcTemplate.query(sql, appUserRowMapper, username);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public AppUser save(AppUser appUser) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, appUser.getUsername(), appUser.getPassword());
        return appUser;
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }
}
