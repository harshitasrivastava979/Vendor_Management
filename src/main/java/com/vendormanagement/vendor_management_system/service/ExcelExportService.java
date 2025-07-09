package com.vendormanagement.vendor_management_system.service;

import com.vendormanagement.vendor_management_system.dto.InvoiceExcelDto;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelExportService {

    public ByteArrayInputStream generateInvoiceExcel(List<InvoiceExcelDto> invoiceDtos) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Invoices");

            // Header
            String[] headers = {
                    "Transaction Type (N – NFET, R – RTGS & for I - within HDFC transfer)",
                    "Beneficiary Code (Mandatory In case of within HDFC transfer and need to metion benefiicary account number)",
                    "Beneficiary Account Number",
                    "Amount to be transferred",
                    "Beneficiary Name (Upto 40 character without any special character)",
                    "Drawee Location",
                    "Print Location",
                    "Bene Address 1",
                    "Bene Address 2",
                    "Bene Address 3",
                    "Bene Address 4",
                    "Bene Address 5",
                    "Instruction Reference Number",
                    "Customer Reference Number(Which needs to be reflected in statement upto character upto 20)",
                    "Payment details 1",
                    "Payment details 2",
                    "Payment details 3",
                    "Payment details 4",
                    "Payment details 5",
                    "Payment details 6",
                    "Payment details 7",
                    "Cheque Number",
                    "Transaction Date (DD/MM/YYYY)",
                    "MICR Number",
                    "IFSC Code",
                    "Beneficiary Bank Name",
                    "Beneficiary Bank Branch Name",
                    "Beneficiary email id"
            };
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < headers.length; col++) {
                headerRow.createCell(col).setCellValue(headers[col]);
            }

            // Data rows
            int rowIdx = 1;
            for (InvoiceExcelDto dto : invoiceDtos) {
                Row row = sheet.createRow(rowIdx++);

                // Map transaction type to N/R/I
                String txnType = "";
                if (dto.getTransactionType() != null) {
                    switch (dto.getTransactionType()) {
                        case NEFT: txnType = "N"; break;
                        case RTGS: txnType = "R"; break;
                        case IFT:  txnType = "I"; break;
                        default: txnType = dto.getTransactionType().name(); // fallback
                    }
                }
                row.createCell(0).setCellValue(txnType);
                row.createCell(1).setCellValue(dto.getBeneficiaryCode());
                row.createCell(2).setCellValue(dto.getBeneAccNum()); //account number
                row.createCell(3).setCellValue(dto.getAmountToBeTransferred() != null ? dto.getAmountToBeTransferred().toString() : "");
                row.createCell(4).setCellValue(dto.getBeneName());

                for (int i = 5; i <= 6; i++) {
                    row.createCell(i).setCellValue("");
                }

                row.createCell(7).setCellValue(dto.getBeneAddress());

                for (int i = 8; i <= 12; i++) {
                    row.createCell(i).setCellValue("");
                }
                row.createCell(13).setCellValue(dto.getCustomerRefNo());
                //row.createCell(22).setCellValue(dto.getTransactionDate().toString()); // update
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                row.createCell(22).setCellValue(dto.getTransactionDate() != null ? dto.getTransactionDate().format(formatter) : "");

                row.createCell(24).setCellValue(dto.getIfscCode());
                row.createCell(25).setCellValue(dto.getBankName());
                row.createCell(26).setCellValue(dto.getBranchName());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    public ByteArrayInputStream generateInvoiceCsv(List<InvoiceExcelDto> invoiceDtos) {
        StringBuilder csvData = new StringBuilder();

        // Header row (exact same order as Excel)
        csvData.append(String.join(",", List.of(
                "Transaction Type",
                "Beneficiary Code",
                "Beneficiary Account Number",
                "Amount to be transferred",
                "Beneficiary Name",
                "Drawee Location",
                "Print Location",
                "Bene Address 1",
                "Bene Address 2",
                "Bene Address 3",
                "Bene Address 4",
                "Bene Address 5",
                "Instruction Reference Number",
                "Customer Reference Number",
                "Payment details 1",
                "Payment details 2",
                "Payment details 3",
                "Payment details 4",
                "Payment details 5",
                "Payment details 6",
                "Payment details 7",
                "Cheque Number",
                "Transaction Date (DD/MM/YYYY)",
                "MICR Number",
                "IFSC Code",
                "Beneficiary Bank Name",
                "Beneficiary Bank Branch Name",
                "Beneficiary email id"
        ))).append("\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (InvoiceExcelDto dto : invoiceDtos) {
            csvData.append(String.join(",", List.of(
                    dto.getTransactionType() != null ? dto.getTransactionType().name() : "",
                    dto.getBeneficiaryCode(),
                    dto.getBeneAccNum(),
                    dto.getAmountToBeTransferred() != null ? dto.getAmountToBeTransferred().toString() : "",
                    dto.getBeneName(),
                    "", "",
                    dto.getBeneAddress(),
                    "", "", "", "", "", // Bene Address 2-5 Instruction Reference
                    dto.getCustomerRefNo(),
                    "", "", "", "", "", "", "","",  // Payment details, Cheque number
                    dto.getTransactionDate() != null ? dto.getTransactionDate().format(formatter) : "",
                    "", // MICR
                    dto.getIfscCode(),
                    dto.getBankName(),
                    dto.getBranchName(),
                    ""
            ))).append("\n");
        }

        return new ByteArrayInputStream(csvData.toString().getBytes(StandardCharsets.UTF_8));
    }

}