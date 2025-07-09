package com.vendormanagement.vendor_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vendormanagement.vendor_management_system.dto.InvoiceRequestDto;
import com.vendormanagement.vendor_management_system.entity.Invoice;
import com.vendormanagement.vendor_management_system.mapper.InvoiceMap;
import com.vendormanagement.vendor_management_system.service.InvoiceService;
import com.vendormanagement.vendor_management_system.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.vendormanagement.vendor_management_system.repository.InvoiceRepository;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceMap invoiceMap;
    private final S3Service s3Service;
    private InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceController(InvoiceService invoiceService, InvoiceMap invoiceMap, S3Service s3Service ,InvoiceRepository invoiceRepository) {
        this.invoiceService = invoiceService;
        this.invoiceMap = invoiceMap;
        this.s3Service = s3Service;
        this.invoiceRepository = invoiceRepository;
    }

    @GetMapping
    public ResponseEntity<String> getAllInvoices() {
        return ResponseEntity.ok("This is the Invoice Management System");
    }

    @GetMapping("/by-vendor-service/{vendorServiceId}")
    public ResponseEntity<List<InvoiceRequestDto>> getInvoicesByVendorService(@PathVariable UUID vendorServiceId) {
        List<Invoice> invoices = invoiceService.getInvoicesByVendorService(vendorServiceId);
        List<InvoiceRequestDto> dtos = invoices.stream()
                .map(invoiceMap::mapInvoiceToDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    // ✅ Create invoice + upload bill file
//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<InvoiceRequestDto> createInvoiceWithFile(
//            @RequestPart("dto") InvoiceRequestDto dto,
//            @RequestPart("billFile") MultipartFile billFile) throws IOException {
//
//        // Generate S3 key
//        String fileKey = "bills/" + UUID.randomUUID() + "_" + billFile.getOriginalFilename();
//
//        // Upload to S3
//        s3Service.uploadFile(fileKey, billFile);
//
//        // Set file key in DTO
//        dto.setBillFileKey(fileKey);
//
//        // Save invoice
//        Invoice savedInvoice = invoiceService.createInvoice(dto);
//        InvoiceRequestDto responseDto = invoiceMap.mapInvoiceToDto(savedInvoice);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
//    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InvoiceRequestDto> createInvoiceWithFile(
            @RequestPart("dto") String dtoJson,
            @RequestPart(value = "billFile" ,required = false) MultipartFile billFile) throws IOException {


        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        InvoiceRequestDto dto = mapper.readValue(dtoJson, InvoiceRequestDto.class);
        if (billFile != null) {


            String fileKey = "bills/" + UUID.randomUUID() + "_" + billFile.getOriginalFilename();
            s3Service.uploadFile(fileKey, billFile);
            dto.setBillFileKey(fileKey);
            System.out.println("DTO billFileKey: " + dto.getBillFileKey());
        }

            Invoice savedInvoice = invoiceService.createInvoice(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(invoiceMap.mapInvoiceToDto(savedInvoice));

    }

    // ✅ Download bill file
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadInvoiceBill(@PathVariable UUID id) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        byte[] fileBytes = s3Service.downloadFile(invoice.getBillFileKey());

        String filename = Paths.get(invoice.getBillFileKey()).getFileName().toString();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileBytes);
    }

    // Preview bill (via presigned URL)
    @GetMapping("/{id}/preview")
    public ResponseEntity<String> previewInvoiceBill(@PathVariable UUID id) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        URL presignedUrl = s3Service.generatePresignedUrl(invoice.getBillFileKey());
        return ResponseEntity.ok(presignedUrl.toString());
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteInvoiceBill(@PathVariable UUID id) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        s3Service.deleteFile(invoice.getBillFileKey());
        return ResponseEntity.ok("File deleted from S3.");
    }
    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable UUID invoiceId) {
        invoiceRepository.deleteById(invoiceId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/by-vendor-service/{vendorServiceId}/created-at")
    public ResponseEntity<List<InvoiceRequestDto>> getInvoicesByVendorServiceAndCreatedAt(
            @PathVariable UUID vendorServiceId,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    )
    {
        List<Invoice> invoices = invoiceRepository.findByVendorServiceIdAndCreatedAtBetween(vendorServiceId, from, to);
        List<InvoiceRequestDto> dtos = invoices.stream()
                .map(invoiceMap::mapInvoiceToDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }



}
