package com.vendormanagement.vendor_management_system.controller;


import com.vendormanagement.vendor_management_system.dto.InvoiceExcelDto;
import com.vendormanagement.vendor_management_system.entity.Invoice;
import com.vendormanagement.vendor_management_system.mapper.InvoiceMap;
import com.vendormanagement.vendor_management_system.repository.InvoiceRepository;
import com.vendormanagement.vendor_management_system.service.ExcelExportService;

import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/excel")
public class InvoiceExcelController {

    private final InvoiceRepository invoiceRepository;
    private final ExcelExportService excelExportService;
    private final InvoiceMap invoiceMap;

    public InvoiceExcelController(
            InvoiceRepository invoiceRepository,
            ExcelExportService excelExportService,
            InvoiceMap invoiceMap
    ) {
        this.invoiceRepository = invoiceRepository;
        this.excelExportService = excelExportService;
        this.invoiceMap = invoiceMap;
    }

    @GetMapping("/invoices/download")
    public ResponseEntity<byte[]> downloadExcel(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) UUID vendorServiceId
    ) {
        try {
            List<Invoice> invoices;

            if (vendorServiceId != null && startDate != null && endDate != null) {
                invoices = invoiceRepository.findByVendorServiceIdAndTransactionDateBetween(vendorServiceId, startDate, endDate);
            } else if (startDate != null && endDate != null) {
                invoices = invoiceRepository.findByTransactionDateBetween(startDate, endDate);
            } else if (vendorServiceId != null) {
                invoices = invoiceRepository.findByVendorServiceId(vendorServiceId);
            } else {
                invoices = invoiceRepository.findAll();
            }

            List<InvoiceExcelDto> dtos = invoices.stream()
                    .map(invoiceMap::mapToExcelDto)
                    .toList();

            ByteArrayInputStream in = excelExportService.generateInvoiceExcel(dtos);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=invoices.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(in.readAllBytes());

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/invoices/download-csv")
    public ResponseEntity<InputStreamResource> downloadCsv(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) UUID vendorServiceId
    ) {
        try {
            List<Invoice> invoices;

            if (vendorServiceId != null && startDate != null && endDate != null) {
                invoices = invoiceRepository.findByVendorServiceIdAndTransactionDateBetween(vendorServiceId, startDate, endDate);
            } else if (startDate != null && endDate != null) {
                invoices = invoiceRepository.findByTransactionDateBetween(startDate, endDate);
            } else if (vendorServiceId != null) {
                invoices = invoiceRepository.findByVendorServiceId(vendorServiceId);
            } else {
                invoices = invoiceRepository.findAll();
            }

            List<InvoiceExcelDto> dtos = invoices.stream()
                    .map(invoiceMap::mapToExcelDto)
                    .toList();

            ByteArrayInputStream stream = excelExportService.generateInvoiceCsv(dtos);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=invoices.csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(new InputStreamResource(stream));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/invoices/download/created-at")
    public ResponseEntity<byte[]> downloadExcelByCreatedAt(
            @RequestParam UUID vendorServiceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        try {
            List<Invoice> invoices = invoiceRepository.findByVendorServiceIdAndCreatedAtBetween(vendorServiceId, from, to);
            List<InvoiceExcelDto> dtos = invoices.stream()
                    .map(invoiceMap::mapToExcelDto)
                    .toList();

            ByteArrayInputStream in = excelExportService.generateInvoiceExcel(dtos);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=invoices.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(in.readAllBytes());

        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/invoices/download-csv/created-at")
    public ResponseEntity<InputStreamResource> downloadCsvByCreatedAt(
            @RequestParam UUID vendorServiceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        try {
            List<Invoice> invoices = invoiceRepository.findByVendorServiceIdAndCreatedAtBetween(vendorServiceId, from, to);
            List<InvoiceExcelDto> dtos = invoices.stream()
                    .map(invoiceMap::mapToExcelDto)
                    .toList();

            ByteArrayInputStream stream = excelExportService.generateInvoiceCsv(dtos);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=invoices_created_at.csv");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(new InputStreamResource(stream));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}