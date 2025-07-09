-- Sample data initialization for Vendor Management System
-- This file will be executed after schema.sql

-- Insert sample service types (only if they don't exist)
INSERT INTO service_types (id, name) VALUES
  (gen_random_uuid(), 'Software Development'),
  (gen_random_uuid(), 'Consulting Services'),
  (gen_random_uuid(), 'Maintenance Services'),
  (gen_random_uuid(), 'Training Services'),
  (gen_random_uuid(), 'Support Services')
ON CONFLICT (name) DO NOTHING;
