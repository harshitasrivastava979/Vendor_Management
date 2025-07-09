-- Add beneficiary_code column to vendors table
ALTER TABLE vendors ADD COLUMN beneficiary_code VARCHAR(12);

-- Add comment to explain the column purpose
COMMENT ON COLUMN vendors.beneficiary_code IS 'Beneficiary code required for HDFC Bank transactions'; 