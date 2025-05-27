import React, { useState } from 'react';
import { vendorAPI } from '../services/api';

const VendorForm = ({ onVendorAdded }) => {
  const [formData, setFormData] = useState({
    name: '',
    address: '',
    contact: '',
    gstNumber: '',
    bankAccount: '',
    ifscCode: '',
    neftEnabled: false
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');

    try {
      console.log('Sending data:', formData); // Add this for debugging
      const response = await vendorAPI.createVendor(formData);
      console.log('Response:', response); // Add this for debugging
      setSuccess('Vendor created successfully!');
      setFormData({
        name: '',
        address: '',
        contact: '',
        gstNumber: '',
        bankAccount: '',
        ifscCode: '',
        neftEnabled: false
      });

      // Notify parent component to refresh the list
      if (onVendorAdded) {
        onVendorAdded(response.data);
      }
    } catch (err) {
      console.error('Error creating vendor:', err.response?.data || err.message);
      setError(err.response?.data?.message || 'Failed to create vendor. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="vendor-form">
      <h2>Add New Vendor</h2>

      {error && <div className="error-message">{error}</div>}
      {success && <div className="success-message">{success}</div>}

      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="name">Vendor Name *</label>
          <input
            type="text"
            id="name"
            name="name"
            value={formData.name}
            onChange={handleChange}
            required
            placeholder="Enter vendor name"
          />
        </div>

        <div className="form-group">
          <label htmlFor="address">Address</label>
          <textarea
            id="address"
            name="address"
            value={formData.address}
            onChange={handleChange}
            placeholder="Enter vendor address"
            rows="3"
          />
        </div>

        <div className="form-group">
          <label htmlFor="contact">Contact</label>
          <input
            type="text"
            id="contact"
            name="contact"
            value={formData.contact}
            onChange={handleChange}
            placeholder="Enter contact number"
          />
        </div>

        <div className="form-group">
          <label htmlFor="gstNumber">GST Number</label>
          <input
            type="text"
            id="gstNumber"
            name="gstNumber"
            value={formData.gstNumber}
            onChange={handleChange}
            placeholder="Enter GST Number"
          />
        </div>

        <div className="form-group">
          <label htmlFor="bankAccount">Bank Account</label>
          <input
            type="text"
            id="bankAccount"
            name="bankAccount"
            value={formData.bankAccount}
            onChange={handleChange}
            placeholder="Enter bank account number"
          />
        </div>

        <div className="form-group">
          <label htmlFor="ifscCode">IFSC Code</label>
          <input
            type="text"
            id="ifscCode"
            name="ifscCode"
            value={formData.ifscCode}
            onChange={handleChange}
            placeholder="Enter IFSC code"
          />
        </div>

        <div className="form-group checkbox-group">
          <label>
            <input
              type="checkbox"
              name="neftEnabled"
              checked={formData.neftEnabled}
              onChange={handleChange}
            />
            NEFT Enabled
          </label>
        </div>

        <button type="submit" disabled={loading}>
          {loading ? 'Creating...' : 'Create Vendor'}
        </button>
      </form>
    </div>
  );
};

export default VendorForm;