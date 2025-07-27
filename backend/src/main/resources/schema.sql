-- Database Schema for Vendor Management System
-- Run this script to create all necessary tables

-- Enable UUID extension if not already enabled
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Create service_types table
CREATE TABLE IF NOT EXISTS service_types (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Create vendors table
CREATE TABLE IF NOT EXISTS vendors (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    address TEXT,
    contact VARCHAR(20),
    gst VARCHAR(15),
    tan VARCHAR(10),
    cin VARCHAR(21),
    bank_acc VARCHAR(20),
    bank_acc_type VARCHAR(20),
    bank_name VARCHAR(100),
    branch_add TEXT,
    ifsc VARCHAR(11),
    neft_enabled BOOLEAN DEFAULT FALSE,
    beneficiary_code VARCHAR(12),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Create vendor_services table
CREATE TABLE IF NOT EXISTS vendor_services (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    vendor_id UUID NOT NULL REFERENCES vendors(id) ON DELETE CASCADE,
    service_id UUID NOT NULL REFERENCES service_types(id) ON DELETE CASCADE,
    tds_rate DECIMAL(5,2),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    UNIQUE(vendor_id, service_id)
);

-- Create invoices table
CREATE TABLE IF NOT EXISTS invoices (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    invoice_number VARCHAR(50) UNIQUE NOT NULL,
    transaction_date DATE NOT NULL,
    invoice_month VARCHAR(7) NOT NULL,
    invoice_type VARCHAR(20) NOT NULL,
    vendorservice_id UUID NOT NULL REFERENCES vendor_services(id) ON DELETE CASCADE,
    gst_percent DECIMAL(5,2),
    gst_amount DECIMAL(10,2),
    amount_inclusive DECIMAL(10,2),
    amount_exclusive DECIMAL(10,2),
    tds_percent DECIMAL(5,2),
    tds_amount DECIMAL(10,2),
    final_amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(10),
    payment_status VARCHAR(10),
    bill_file_key VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_vendors_name ON vendors(name);
CREATE INDEX IF NOT EXISTS idx_vendor_services_vendor_id ON vendor_services(vendor_id);
CREATE INDEX IF NOT EXISTS idx_vendor_services_service_id ON vendor_services(service_id);
CREATE INDEX IF NOT EXISTS idx_invoices_vendorservice_id ON invoices(vendorservice_id);
CREATE INDEX IF NOT EXISTS idx_invoices_invoice_month ON invoices(invoice_month);
CREATE INDEX IF NOT EXISTS idx_invoices_transaction_date ON invoices(transaction_date);
CREATE INDEX IF NOT EXISTS idx_invoices_invoice_number ON invoices(invoice_number); 