import React, { useState, useEffect } from 'react';
import { vendorAPI, fetchServiceTypes } from '../../services/api.js';
import './VendorForm.css';

 VendorForm = ({ onVendorAdded }) => {
  const [formData, setFormData] = useState({
    name: '',
    address: '',
    contact: '',
    gstTanCin: '',
    bankAcc: '',
    ifsc: '',
    neftEnabled: false
  });

  const [selectedServices, setSelectedServices] = useState([
    { serviceTypeId: '', tdsRate: '' }
  ]);
  const [serviceTypeOptions, setServiceTypeOptions] = useState([]); // Initialize as empty array

  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState('');
  const [error, setError] = useState('');
  const [validationErrors, setValidationErrors] = useState({});

  useEffect(() => {
    const loadServiceTypes = async () => {
      try {
        console.log('🔄 Fetching service types...');
        const serviceTypes = await fetchServiceTypes();
        console.log('✅ Service types loaded:', serviceTypes);

        // Ensure we always set an array
        if (Array.isArray(serviceTypes)) {
          setServiceTypeOptions(serviceTypes);
        } else if (serviceTypes && Array.isArray(serviceTypes.data)) {
          setServiceTypeOptions(serviceTypes.data);
        } else if (serviceTypes && serviceTypes.serviceTypes && Array.isArray(serviceTypes.serviceTypes)) {
          setServiceTypeOptions(serviceTypes.serviceTypes);
        } else {
          console.warn('Unexpected service types format:', serviceTypes);
          setServiceTypeOptions([]); // Fallback to empty array
        }
      } catch (err) {
        console.error('❌ Failed to fetch service types:', err);
        setError('Failed to load service types. Please check if your backend is running.');
        setServiceTypeOptions([]); // Ensure array on error
      }
    };

    loadServiceTypes();
  }, []);

  const validateForm = () => {
    const errors = {};

    if (!formData.name.trim()) {
      errors.name = 'Vendor name is required';
    } else if (formData.name.length < 2) {
      errors.name = 'Name must be at least 2 characters';
    } else if (formData.name.length > 100) {
      errors.name = 'Name cannot exceed 100 characters';
    }

    if (formData.contact && !/^[6-9]\d{9}$/.test(formData.contact)) {
      errors.contact = 'Invalid mobile number (10 digits starting with 6-9)';
    }

    if (formData.gstTanCin) {
      const gstRegex = /^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$/;
      if (!gstRegex.test(formData.gstTanCin)) {
        errors.gstTanCin = 'Invalid GST format (15 characters: 22AAAAA0000A1Z5)';
      }
    }

    if (formData.bankAcc && !/^\d{9,18}$/.test(formData.bankAcc)) {
      errors.bankAcc = 'Bank account must be 9-18 digits';
    }

    if (formData.ifsc && !/^[A-Z]{4}0[A-Z0-9]{6}$/.test(formData.ifsc)) {
      errors.ifsc = 'Invalid IFSC format (ABCD0123456)';
    }

    // Validate services
    const validServices = selectedServices.filter(service =>
      service.serviceTypeId && service.tdsRate
    );

    if (validServices.length === 0) {
      errors.services = 'At least one service with TDS rate is required';
    }

    return errors;
  };

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    let processedValue = value;

    if (name === 'gstTanCin' || name === 'ifsc') {
      processedValue = value.toUpperCase();
    }

    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : processedValue
    }));

    if (validationErrors[name]) {
      setValidationErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

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

    // Filter out empty services and convert tdsRate to number
    const validServices = selectedServices
      .filter(service => service.serviceTypeId && service.tdsRate)
      .map(service => ({
        serviceTypeId: service.serviceTypeId,
        tdsRate: parseFloat(service.tdsRate)
      }));

    const payload = {
      ...formData,
      vendorServiceList: validServices
    };

    console.log('🚀 Sending payload:', payload);

    try {
      const createdVendor = await vendorAPI.createVendor(payload);
      setSuccess('Vendor created successfully!');
      setFormData({
        name: '',
        address: '',
        contact: '',
        gstTanCin: '',
        bankAcc: '',
        ifsc: '',
        neftEnabled: false
      });
      setSelectedServices([{ serviceTypeId: '', tdsRate: '' }]);

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

  const testConnection = async () => {
    setLoading(true);
    setError('');
    setSuccess('');

    try {
      // Test both service types endpoint and vendor endpoint
      const serviceTypes = await fetchServiceTypes();

      // Handle different response formats
      let count = 0;
      if (Array.isArray(serviceTypes)) {
        count = serviceTypes.length;
      } else if (serviceTypes && Array.isArray(serviceTypes.data)) {
        count = serviceTypes.data.length;
      } else if (serviceTypes && serviceTypes.serviceTypes && Array.isArray(serviceTypes.serviceTypes)) {
        count = serviceTypes.serviceTypes.length;
      }

      setSuccess(`✅ Connection successful! Found ${count} service types.`);
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

      {success && <div className="alert alert-success">✓ {success}</div>}
      {error && <div className="alert alert-error">✗ {error}</div>}

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
          {validationErrors.name && <div className="field-error">{validationErrors.name}</div>}
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
          {validationErrors.contact && <div className="field-error">{validationErrors.contact}</div>}
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
          {validationErrors.gstTanCin && <div className="field-error">{validationErrors.gstTanCin}</div>}
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
            {validationErrors.bankAcc && <div className="field-error">{validationErrors.bankAcc}</div>}
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
            {validationErrors.ifsc && <div className="field-error">{validationErrors.ifsc}</div>}
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

        {/* Service type and TDS rate section */}
        <h3>Services & TDS</h3>
        {validationErrors.services && <div className="field-error">{validationErrors.services}</div>}

        {selectedServices.map((service, index) => (
          <div className="service-entry" key={index}>
            <div className="form-group">
              <label>Service Type</label>
              <select
                value={service.serviceTypeId}
                onChange={(e) => {
                  const updated = [...selectedServices];
                  updated[index].serviceTypeId = e.target.value;
                  setSelectedServices(updated);
                }}
                className={validationErrors.services ? 'error' : ''}
              >
                <option value="">-- Select Service Type --</option>
                {/* Ensure serviceTypeOptions is always an array before mapping */}
                {Array.isArray(serviceTypeOptions) && serviceTypeOptions.map((type) => (
                  <option key={type.id} value={type.id}>
                    {type.name}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label>TDS Rate (%)</label>
              <input
                type="number"
                value={service.tdsRate}
                onChange={(e) => {
                  const updated = [...selectedServices];
                  updated[index].tdsRate = e.target.value;
                  setSelectedServices(updated);
                }}
                placeholder="e.g. 5.0"
                min="0"
                max="30"
                step="0.1"
                className={validationErrors.services ? 'error' : ''}
              />
            </div>

            {selectedServices.length > 1 && (
              <button
                type="button"
                onClick={() => {
                  const updated = [...selectedServices];
                  updated.splice(index, 1);
                  setSelectedServices(updated);
                }}
                className="remove-service-btn"
              >
                Remove Service
              </button>
            )}
          </div>
        ))}

        <button
          type="button"
          onClick={() =>
            setSelectedServices([...selectedServices, { serviceTypeId: '', tdsRate: '' }])
          }
          className="add-service-btn"
        >
          + Add Another Service
        </button>

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

      {/* Debug information */}
      {process.env.NODE_ENV === 'development' && (
        <div style={{ marginTop: '20px', padding: '10px', backgroundColor: '#f0f0f0', fontSize: '12px' }}>
          <strong>Debug Info:</strong>
          <br />
          Service Types Count: {Array.isArray(serviceTypeOptions) ? serviceTypeOptions.length : 'Not an array'}
          <br />
          Service Types Type: {typeof serviceTypeOptions}
          <br />
          Service Types: {JSON.stringify(serviceTypeOptions, null, 2)}
        </div>
      )}
    </div>
  );
};

export default VendorForm;