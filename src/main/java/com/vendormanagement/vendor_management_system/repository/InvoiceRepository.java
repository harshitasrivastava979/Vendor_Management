package com.vendormanagement.vendor_management_system.repository;

import com.vendormanagement.vendor_management_system.entity.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InvoiceRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Invoice> invoiceRowMapper = new RowMapper<Invoice>() {
        @Override
        public Invoice mapRow(ResultSet rs, int rowNum) throws SQLException {
            Invoice invoice = new Invoice();
            invoice.setId(UUID.fromString(rs.getString("id")));
            invoice.setInvoiceNumber(rs.getString("invoice_number"));
            invoice.setTransactionDate(rs.getDate("transaction_date").toLocalDate());
            invoice.setInvoiceMonth(rs.getString("invoice_month"));
            invoice.setInvoiceType(Invoice.InvoiceType.valueOf(rs.getString("invoice_type")));
            invoice.setGstPercent(rs.getBigDecimal("gst_percent"));
            invoice.setGstAmount(rs.getBigDecimal("gst_amount"));
            invoice.setAmountInclusive(rs.getBigDecimal("amount_inclusive"));
            invoice.setAmountExclusive(rs.getBigDecimal("amount_exclusive"));
            invoice.setTdsPercent(rs.getBigDecimal("tds_percent"));
            invoice.setTdsAmount(rs.getBigDecimal("tds_amount"));
            invoice.setFinalAmount(rs.getBigDecimal("final_amount"));
            
            String paymentMethodStr = rs.getString("payment_method");
            if (paymentMethodStr != null) {
                invoice.setPaymentMethod(Invoice.PaymentMethod.valueOf(paymentMethodStr));
            }
            
            String paymentStatusStr = rs.getString("payment_status");
            if (paymentStatusStr != null) {
                invoice.setPaymentStatus(Invoice.PaymentStatus.valueOf(paymentStatusStr));
            }
            
            invoice.setBillFileKey(rs.getString("bill_file_key"));
            invoice.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            invoice.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            
            // Note: vendorService relationship would need to be loaded separately
            // or you can add a join query if needed
            
            return invoice;
        }
    };

    public List<Invoice> findAll() {
        String sql = "SELECT * FROM invoices ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, invoiceRowMapper);
    }

    public Optional<Invoice> findById(UUID id) {
        String sql = "SELECT * FROM invoices WHERE id = ?";
        List<Invoice> results = jdbcTemplate.query(sql, invoiceRowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Invoice save(Invoice invoice) {
        if (invoice.getId() == null) {
            // Insert new invoice
            invoice.setId(UUID.randomUUID());
            String sql = """
                INSERT INTO invoices (id, invoice_number, transaction_date, invoice_month, invoice_type,
                gst_percent, gst_amount, amount_inclusive, amount_exclusive, tds_percent, tds_amount,
                final_amount, payment_method, payment_status, bill_file_key, vendorservice_id, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
                """;
            jdbcTemplate.update(sql,
                invoice.getId(),
                invoice.getInvoiceNumber(),
                invoice.getTransactionDate(),
                invoice.getInvoiceMonth(),
                invoice.getInvoiceType().name(),
                invoice.getGstPercent(),
                invoice.getGstAmount(),
                invoice.getAmountInclusive(),
                invoice.getAmountExclusive(),
                invoice.getTdsPercent(),
                invoice.getTdsAmount(),
                invoice.getFinalAmount(),
                invoice.getPaymentMethod() != null ? invoice.getPaymentMethod().name() : null,
                invoice.getPaymentStatus() != null ? invoice.getPaymentStatus().name() : null,
                invoice.getBillFileKey(),
                invoice.getVendorService() != null ? invoice.getVendorService().getId() : null
            );
        } else {
            // Update existing invoice
            String sql = """
                UPDATE invoices SET invoice_number = ?, transaction_date = ?, invoice_month = ?, invoice_type = ?,
                gst_percent = ?, gst_amount = ?, amount_inclusive = ?, amount_exclusive = ?, tds_percent = ?, 
                tds_amount = ?, final_amount = ?, payment_method = ?, payment_status = ?, bill_file_key = ?,
                vendorservice_id = ?, updated_at = NOW()
                WHERE id = ?
                """;
            jdbcTemplate.update(sql,
                invoice.getInvoiceNumber(),
                invoice.getTransactionDate(),
                invoice.getInvoiceMonth(),
                invoice.getInvoiceType().name(),
                invoice.getGstPercent(),
                invoice.getGstAmount(),
                invoice.getAmountInclusive(),
                invoice.getAmountExclusive(),
                invoice.getTdsPercent(),
                invoice.getTdsAmount(),
                invoice.getFinalAmount(),
                invoice.getPaymentMethod() != null ? invoice.getPaymentMethod().name() : null,
                invoice.getPaymentStatus() != null ? invoice.getPaymentStatus().name() : null,
                invoice.getBillFileKey(),
                invoice.getVendorService() != null ? invoice.getVendorService().getId() :null,
                invoice.getId()
            );
        }
        return invoice;
    }

    public void deleteById(UUID id) {
        String sql = "DELETE FROM invoices WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM invoices WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    public boolean existsByInvoiceNumber(String invoiceNumber) {
        String sql = "SELECT COUNT(*) FROM invoices WHERE invoice_number = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, invoiceNumber);
        return count != null && count > 0;
    }

    public List<Invoice> findByVendorServiceId(UUID vendorServiceId) {
        String sql = "SELECT * FROM invoices WHERE vendorservice_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, invoiceRowMapper, vendorServiceId);
    }

    public List<Invoice> findByInvoiceMonthBetween(String startDate, String endDate) {
        String sql = "SELECT * FROM invoices WHERE invoice_month BETWEEN ? AND ? ORDER BY invoice_month";
        return jdbcTemplate.query(sql, invoiceRowMapper, startDate, endDate);
    }

    public List<Invoice> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM invoices WHERE transaction_date BETWEEN ? AND ? ORDER BY transaction_date";
        return jdbcTemplate.query(sql, invoiceRowMapper, startDate, endDate);
    }

    public List<Invoice> findByVendorAndDateRange(UUID vendorId, LocalDate start, LocalDate end) {
        String sql = """
            SELECT i.* FROM invoices i 
            JOIN vendor_services vs ON i.vendorservice_id = vs.id 
            WHERE vs.vendor_id = ? AND i.invoice_month BETWEEN ? AND ?
            ORDER BY i.invoice_month
            """;
        return jdbcTemplate.query(sql, invoiceRowMapper, vendorId, start, end);
    }

    public List<Invoice> findByVendorServiceIdAndTransactionDateBetween(UUID vendorServiceId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM invoices WHERE vendorservice_id = ? AND transaction_date BETWEEN ? AND ? ORDER BY transaction_date";
        return jdbcTemplate.query(sql, invoiceRowMapper, vendorServiceId, startDate, endDate);
    }

    public List<Invoice> findByVendorServiceIdAndCreatedAtBetween(UUID vendorServiceId, LocalDateTime from, LocalDateTime to) {
        String sql = "SELECT * FROM invoices WHERE vendorservice_id = ? AND created_at BETWEEN ? AND ? ORDER BY created_at";
        return jdbcTemplate.query(sql, invoiceRowMapper, vendorServiceId, from, to);
    }
}