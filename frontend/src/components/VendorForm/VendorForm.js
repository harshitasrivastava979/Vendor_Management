// Update your src/components/VendorForm/VendorForm.js with this version

import React, { useState } from 'react';
import { vendorAPI } from '../../services/api.js';
import './VendorForm.css';

const VendorForm = ({ onVendorAdded }) => {
  const [formData, setFormData] = useState({
    name: '',
    address: '',
    contact: '',
    gstTanCin: '',
    bankAcc: '',
    ifsc: '',
    neftEnabled: false
  });

  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState('');
  const [error, setError] = useState('');
  const [validationErrors, setValidationErrors] = useState({});

  const validateForm = () => {
    const errors = {};

    // Name validation
    if (!formData.name.trim()) {
      errors.name = 'Vendor name is required';
    } else if (formData.name.length < 2) {
      errors.name = 'Name must be at least 2 characters';
    } else if (formData.name.length > 100) {
      errors.name = 'Name cannot exceed 100 characters';
    }

    // Contact validation (Indian mobile format)
    if (formData.contact && !/^[6-9]\d{9}$/.test(formData.contact)) {
      errors.contact = 'Invalid mobile number (10 digits starting with 6-9)';
    }

    // GST validation (Indian format)
    if (formData.gstTanCin) {
      const gstRegex = /^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$/;
      if (!gstRegex.test(formData.gstTanCin)) {
        errors.gstTanCin = 'Invalid GST format (15 characters: 22AAAAA0000A1Z5)';
      }
    }

    // Bank account validation
    if (formData.bankAcc && !/^\d{9,18}$/.test(formData.bankAcc)) {
      errors.bankAcc = 'Bank account must be 9-18 digits';
    }

    // IFSC validation
    if (formData.ifsc && !/^[A-Z]{4}0[A-Z0-9]{6}$/.test(formData.ifsc)) {
      errors.ifsc = 'Invalid IFSC format (ABCD0123456)';
    }

    return errors;
  };

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    let processedValue = value;

    // Auto-uppercase for GST and IFSC
    if (name === 'gstTanCin' || name === 'ifsc') {
      processedValue = value.toUpperCase();
    }

    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : processedValue
    }));

    // Clear validation error when user starts typing
    if (validationErrors[name]) {
      setValidationErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validate form
    const errors = validateForm();
    if (Object.keys(errors).length > 0) {
      setValidationErrors(errors);
      setError('Please fix the validation errors');
      return;
    }

    setLoading(true);
    setError('');
    setSuccess('');
    setValidationErrors({});

    try {
      console.log('📤 Sending vendor data to Spring Boot:', formData);

      // Call real API
      const createdVendor = await vendorAPI.createVendor(formData);

      console.log('✅ Vendor created successfully:', createdVendor);

      setSuccess('Vendor created successfully!');

      // Clear form
      setFormData({
        name: '',
        address: '',
        contact: '',
        gstTanCin: '',
        bankAcc: '',
        ifsc: '',
        neftEnabled: false
      });

      // Notify parent component
      if (onVendorAdded) {
        onVendorAdded(createdVendor);
      }

    } catch (err) {
      console.error('❌ Error creating vendor:', err);
      setError(err.message || 'Failed to create vendor');
    } finally {
      setLoading(false);
    }
  };

  // Test connection function
  const testConnection = async () => {
    setLoading(true);
    setError('');
    setSuccess('');

    try {
      const result = await vendorAPI.testConnection();
      setSuccess(`✅ Connection successful: ${result}`);
    } catch (err) {
      setError(`❌ Connection failed: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="vendor-form">
      <div className="form-header">
        <h2>Add New Vendor</h2>
        <button
          type="button"
          onClick={testConnection}
          className="test-btn"
          disabled={loading}
        >
          Test API
        </button>
      </div>

      {success && (
        <div className="alert alert-success">
          ✓ {success}
        </div>
      )}

      {error && (
        <div className="alert alert-error">
          ✗ {error}
        </div>
      )}

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="name">Vendor Name *</label>
          <input
            type="text"
            id="name"
            name="name"
            value={formData.name}
            onChange={handleChange}
            className={validationErrors.name ? 'error' : ''}
            placeholder="Enter vendor name"
          />
          {validationErrors.name && (
            <div className="field-error">{validationErrors.name}</div>
          )}
        </div>

        <div className="form-group">
          <label htmlFor="address">Address</label>
          <textarea
            id="address"
            name="address"
            value={formData.address}
            onChange={handleChange}
            rows="3"
            placeholder="Enter vendor address"
          />
        </div>

        <div className="form-group">
          <label htmlFor="contact">Contact Number</label>
          <input
            type="tel"
            id="contact"
            name="contact"
            value={formData.contact}
            onChange={handleChange}
            className={validationErrors.contact ? 'error' : ''}
            placeholder="9876543210"
            maxLength="10"
          />
          {validationErrors.contact && (
            <div className="field-error">{validationErrors.contact}</div>
          )}
        </div>

        <div className="form-group">
          <label htmlFor="gstTanCin">GST Number</label>
          <input
            type="text"
            id="gstTanCin"
            name="gstTanCin"
            value={formData.gstTanCin}
            onChange={handleChange}
            className={validationErrors.gstTanCin ? 'error' : ''}
            placeholder="22AAAAA0000A1Z5"
            maxLength="15"
          />
          {validationErrors.gstTanCin && (
            <div className="field-error">{validationErrors.gstTanCin}</div>
          )}
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="bankAcc">Bank Account</label>
            <input
              type="text"
              id="bankAcc"
              name="bankAcc"
              value={formData.bankAcc}
              onChange={handleChange}
              className={validationErrors.bankAcc ? 'error' : ''}
              placeholder="123456789012"
            />
            {validationErrors.bankAcc && (
              <div className="field-error">{validationErrors.bankAcc}</div>
            )}
          </div>

          <div className="form-group">
            <label htmlFor="ifsc">IFSC Code</label>
            <input
              type="text"
              id="ifsc"
              name="ifsc"
              value={formData.ifsc}
              onChange={handleChange}
              className={validationErrors.ifsc ? 'error' : ''}
              placeholder="HDFC0001234"
              maxLength="11"
            />
            {validationErrors.ifsc && (
              <div className="field-error">{validationErrors.ifsc}</div>
            )}
          </div>
        </div>

        <div className="form-group checkbox-group">
          <label className="checkbox-label">
            <input
              type="checkbox"
              name="neftEnabled"
              checked={formData.neftEnabled}
              onChange={handleChange}
            />
            <span className="checkmark"></span>
            NEFT Enabled
          </label>
        </div>

        <button type="submit" disabled={loading} className="submit-btn">
          {loading ? (
            <>
              <span className="spinner"></span>
              Creating...
            </>
          ) : (
            'Create Vendor'
          )}
        </button>
      </form>
    </div>
  );
};

export default VendorForm;