import React, { useState, useEffect } from 'react';
import { vendorAPI, fetchServiceTypes } from '../../services/api';
import './VendorForm.css';

const VendorForm = ({ onVendorAdded, editingVendor, onVendorUpdated }) => {
  const [formData, setFormData] = useState({
    name: '',
    address: '',
    contact: '',
    gst: '',
    tan: '',
    cin: '',
    bankAcc: '',
    bankAccType: '',
    bankName: '',
    branchAdd: '',
    ifsc: '',
    neftEnabled: false,
    beneficiaryCode: ''
  });

  const [selectedServices, setSelectedServices] = useState([
    { serviceTypeId: '', tdsRate: '', usedInInvoices: false, id: null }
  ]);
  const [serviceTypeOptions, setServiceTypeOptions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState('');
  const [error, setError] = useState('');
  const [validationErrors, setValidationErrors] = useState({});
  const [showServiceEditor, setShowServiceEditor] = useState(false);

  // Check if HDFC Bank is selected
  const isHdfcBank = formData.bankName.toUpperCase().includes('HDFC');

  // Common bank names for easier selection
  const commonBanks = [
    'AU SMALL FINANCE BANK LTD.',
    'AXIS BANK',
    'AXIS BANK LTD.',
    'BANDHAN BANK LTD.',
    'BANK OF BARODA',
    'BANK OF INDIA',
    'BANK OF MAHARASHTRA',
    'CANARA BANK',
    'CAPITAL SMALL FINANCE BANK LTD.',
    'CENTRAL BANK OF INDIA',
    'CITY UNION BANK LTD.',
    'CSB BANK LIMITED',
    'DCB BANK LTD.',
    'DHANLAXMI BANK LTD.',
    'FEDERAL BANK LTD.',
    'HDFC BANK',
    'HDFC BANK LTD',
    'ICICI BANK',
    'ICICI BANK LTD.',
    'IDBI BANK LIMITED',
    'IDFC FIRST BANK LIMITED',
    'INDIAN BANK',
    'INDIAN OVERSEAS BANK',
    'INDUSIND BANK LTD',
    'JAMMU & KASHMIR BANK LTD.',
    'KOTAK MAHINDRA BANK LTD',
    'PUNJAB & SIND BANK',
    'PUNJAB NATIONAL BANK',
    'RBL BANK LTD.',
    'STATE BANK OF INDIA',
    'UCO BANK',
    'UNION BANK OF INDIA',
    'YES BANK LTD.'
  ];

  // Pre-fill form if editing
  useEffect(() => {
    if (editingVendor) {
      console.log('🔄 Editing vendor data received:', editingVendor);
      
      setFormData({
        name: editingVendor.name || '',
        address: editingVendor.address || '',
        contact: editingVendor.contact || '',
        gst: editingVendor.gst || '',
        tan: editingVendor.tan || '',
        cin: editingVendor.cin || '',
        bankAcc: editingVendor.bankAcc || '',
        bankAccType: editingVendor.bankAccType || '',
        bankName: editingVendor.bankName || '',
        branchAdd: editingVendor.branchAdd || '',
        ifsc: editingVendor.ifsc || '',
        neftEnabled: editingVendor.neftEnabled || false,
        beneficiaryCode: editingVendor.beneficiaryCode || ''
      });

      // Enhanced service handling for editing
      if (editingVendor.vendorServiceList && Array.isArray(editingVendor.vendorServiceList)) {
        console.log('📋 Vendor services received:', editingVendor.vendorServiceList);
        
        setSelectedServices(editingVendor.vendorServiceList.map(s => ({
          id: s.id || null,
          serviceTypeId: s.serviceType?.id || s.serviceTypeId || '',
          tdsRate: s.tdsRate || '',
          usedInInvoices: s.usedInInvoices || false,
          isExisting: true // Flag to identify existing services
        })));
      } else {
        console.log('⚠️ No vendor services found in editing data');
        setSelectedServices([{ serviceTypeId: '', tdsRate: '', usedInInvoices: false, id: null, isExisting: false }]);
      }
      setShowServiceEditor(false); // lock by default in edit mode
    } else {
      // Reset form for new vendor
      setFormData({
        name: '',
        address: '',
        contact: '',
        gst: '',
        tan: '',
        cin: '',
        bankAcc: '',
        bankAccType: '',
        bankName: '',
        branchAdd: '',
        ifsc: '',
        neftEnabled: false,
        beneficiaryCode: ''
      });
      setSelectedServices([{ serviceTypeId: '', tdsRate: '', usedInInvoices: false, id: null, isExisting: false }]);
      setShowServiceEditor(true); // open by default in create mode
    }
  }, [editingVendor]);

  useEffect(() => {
    const loadServiceTypes = async () => {
      try {
        console.log('🔄 Fetching service types...');
        const serviceTypes = await fetchServiceTypes();
        console.log('✅ Service types loaded:', serviceTypes);

        if (Array.isArray(serviceTypes)) {
          setServiceTypeOptions(serviceTypes);
        } else if (serviceTypes && Array.isArray(serviceTypes.data)) {
          setServiceTypeOptions(serviceTypes.data);
        } else if (serviceTypes && serviceTypes.serviceTypes && Array.isArray(serviceTypes.serviceTypes)) {
          setServiceTypeOptions(serviceTypes.serviceTypes);
        } else {
          console.warn('Unexpected service types format:', serviceTypes);
          setServiceTypeOptions([]);
        }
      } catch (err) {
        console.error('❌ Failed to fetch service types:', err);
        if (err.message === 'Network error') {
          setError('❌ Backend server is not running. Please start the Spring Boot application on port 8080.');
        } else {
          setError(`Failed to load service types: ${err.message}`);
        }
        setServiceTypeOptions([]);
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
    } else if (formData.name.length > 40) {
      errors.name = 'Name cannot exceed 40 characters';
    } else if (!/^[a-zA-Z0-9\s]+$/.test(formData.name)) {
      errors.name = 'Name must not contain special characters';
    }

    if (formData.contact && !/^[6-9]\d{9}$/.test(formData.contact)) {
      errors.contact = 'Invalid mobile number (10 digits starting with 6-9)';
    }

    if (formData.gst && !/^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$/.test(formData.gst)) {
      errors.gst = 'Invalid GST format (15 characters: 22AAAAA0000A1Z5)';
    }

    if (formData.tan && !/^[A-Z]{4}[0-9]{5}[A-Z]{1}$/.test(formData.tan)) {
      errors.tan = 'Invalid TAN format (10 characters: AAAA12345B)';
    }

    if (formData.cin && !/^[A-Z]{1}[0-9]{5}[A-Z]{2}[0-9]{4}[A-Z]{3}[0-9]{6}$/.test(formData.cin)) {
      errors.cin = 'Invalid CIN format (21 characters: L12345AB1234567890123)';
    }

    if (formData.bankAcc && !/^\d{9,18}$/.test(formData.bankAcc)) {
      errors.bankAcc = 'Bank account must be 9-18 digits';
    }

    if (formData.ifsc && !/^[A-Z]{4}0[A-Z0-9]{6}$/.test(formData.ifsc)) {
      errors.ifsc = 'Invalid IFSC format (ABCD0123456)';
    }

    if (isHdfcBank && !formData.beneficiaryCode.trim()) {
      errors.beneficiaryCode = 'Beneficiary Code is required for HDFC Bank';
    } else if (isHdfcBank && formData.beneficiaryCode && !/^[A-Z0-9]{6,12}$/.test(formData.beneficiaryCode)) {
      errors.beneficiaryCode = 'Beneficiary Code must be 6-12 alphanumeric characters';
    }

    const validServices = selectedServices.filter(service =>
      service.serviceTypeId && service.serviceTypeId.trim() && service.tdsRate
    );

    if (validServices.length === 0) {
      errors.services = 'At least one service with TDS rate is required';
    }

    return errors;
  };

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    let processedValue = value;

    if (['gst', 'tan', 'cin', 'ifsc', 'bankName', 'beneficiaryCode'].includes(name)) {
      processedValue = value.toUpperCase();
    }

    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : processedValue
    }));

    if (name === 'bankName' && !processedValue.toUpperCase().includes('HDFC')) {
      setFormData(prev => ({
        ...prev,
        beneficiaryCode: ''
      }));
    }

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

    const validServices = selectedServices
      .filter(service => service.serviceTypeId && service.serviceTypeId.trim() && service.tdsRate)
      .map(service => {
        // Validate UUID format
        const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
        if (!uuidRegex.test(service.serviceTypeId.trim())) {
          throw new Error(`Invalid service type ID format: ${service.serviceTypeId}`);
        }
        return {
          serviceTypeId: service.serviceTypeId.trim(),
          tdsRate: parseFloat(service.tdsRate)
        };
      });

    const payload = {
      ...formData,
      vendorServiceList: validServices
    };

    console.log('Sending payload to backend:', payload);

    try {
      let result;
      if (editingVendor && editingVendor.id) {
        result = await vendorAPI.updateVendor(editingVendor.id, payload);
        setSuccess('Vendor updated successfully!');
        if (onVendorUpdated) onVendorUpdated(result);
      } else {
        result = await vendorAPI.createVendor(payload);
        setSuccess('Vendor created successfully!');
        if (onVendorAdded) onVendorAdded(result);
      }

      // Reset form after successful submission
      setFormData({
        name: '',
        address: '',
        contact: '',
        gst: '',
        tan: '',
        cin: '',
        bankAcc: '',
        bankAccType: '',
        bankName: '',
        branchAdd: '',
        ifsc: '',
        neftEnabled: false,
        beneficiaryCode: ''
      });
      setSelectedServices([{ serviceTypeId: '', tdsRate: '', usedInInvoices: false, id: null, isExisting: false }]);
    } catch (err) {
      console.error('Error saving vendor:', err);
      
      // Handle specific backend errors
      if (err.message && err.message.includes('Cannot change service type for service used in invoices')) {
        setError('❌ Cannot change service type for services used in invoices. Only TDS rates can be updated for such services.');
      } else if (err.message && err.message.includes('Service Type not found')) {
        setError('❌ Invalid service type selected. Please refresh the page and try again.');
      } else if (err.message && err.message.includes('Service Type ID cannot be null')) {
        setError('❌ Please select a service type for all services.');
      } else {
        setError(err.message || 'Failed to save vendor');
      }
    } finally {
      setLoading(false);
    }
  };

  const testConnection = async () => {
    setLoading(true);
    setError('');
    setSuccess('');

    try {
      const serviceTypes = await fetchServiceTypes();

      let count = 0;
      if (Array.isArray(serviceTypes)) {
        count = serviceTypes.length;
      } else if (serviceTypes && Array.isArray(serviceTypes.data)) {
        count = serviceTypes.data.length;
      } else if (serviceTypes && serviceTypes.serviceTypes && Array.isArray(serviceTypes.serviceTypes)) {
        count = serviceTypes.serviceTypes.length;
      }

      setSuccess(`✅ Backend connection successful! Found ${count} service types.`);
    } catch (err) {
      if (err.message === 'Network error') {
        setError('❌ Backend server is not running. Please start the Spring Boot application on port 8080.');
      } else {
        setError(`❌ Connection failed: ${err.message}`);
      }
    } finally {
      setLoading(false);
    }
  };

  const addService = () => {
    // Prevent adding if not in edit mode
    if (!showServiceEditor) {
      setError('Please click "Edit Services" to add new services.');
      return;
    }

    setSelectedServices([...selectedServices, {
      serviceTypeId: '',
      tdsRate: '',
      usedInInvoices: false,
      id: null,
      isExisting: false
    }]);
  };

  const removeService = (index) => {
    const serviceToRemove = selectedServices[index];

    // Prevent removal if not in edit mode
    if (!showServiceEditor) {
      setError('Please click "Edit Services" to modify service information.');
      return;
    }

    // Prevent removal if service is used in invoices
    if (serviceToRemove.usedInInvoices) {
      setError('Cannot remove service that is used in existing invoices. Only TDS rate can be updated.');
      return;
    }

    if (selectedServices.length > 1) {
      setSelectedServices(selectedServices.filter((_, i) => i !== index));
    }
  };

  const handleServiceChange = (index, field, value) => {
    const service = selectedServices[index];

    // Prevent changes if not in edit mode
    if (!showServiceEditor) {
      setError('Please click "Edit Services" to modify service information.');
      return;
    }

    // Prevent service type change if service is used in invoices
    if (field === 'serviceTypeId' && service.usedInInvoices) {
      setError('Cannot change service type for services used in existing invoices. Only TDS rate can be updated.');
      return;
    }

    const updatedServices = [...selectedServices];
    updatedServices[index][field] = value;
    setSelectedServices(updatedServices);

    // Clear any previous errors
    if (error.includes('Cannot change service type') || error.includes('Cannot remove service') || error.includes('Please click')) {
      setError('');
    }
  };

  const getServiceTypeName = (serviceTypeId) => {
    const serviceType = serviceTypeOptions.find(type => type.id === serviceTypeId);
    return serviceType ? serviceType.name : 'Unknown Service';
  };

  const handleServiceEditorToggle = () => {
    // Only toggle if no services are used in invoices
    if (!selectedServices.some(service => service.usedInInvoices)) {
      setShowServiceEditor(!showServiceEditor);
    }
  };

  return (
    <div className="vendor-form">
      <div className="form-header">
        <h2>{editingVendor ? 'Update Vendor' : 'Add New Vendor'}</h2>
      </div>

      {success && <div className="alert alert-success">✓ {success}</div>}
      {error && <div className="alert alert-error">✗ {error}</div>}

      <form onSubmit={handleSubmit}>
        {/* Basic Information Section */}
        <div className="form-section">
          <h3>Basic Information</h3>

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
              required
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
              placeholder="Enter vendor address"
              rows="3"
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
              placeholder="Enter 10-digit mobile number"
            />
            {validationErrors.contact && <div className="field-error">{validationErrors.contact}</div>}
          </div>
        </div>

        {/* Tax Information Section */}
        <div className="form-section">
          <h3>Tax Information</h3>

          <div className="form-group">
            <label htmlFor="gst">GST Number *</label>
            <input
              type="text"
              id="gst"
              name="gst"
              value={formData.gst}
              onChange={handleChange}
              className={validationErrors.gst ? 'error' : ''}
              placeholder="22AAAAA0000A1Z5"
              maxLength="15"
              required
            />
            {validationErrors.gst && <div className="field-error">{validationErrors.gst}</div>}
          </div>

          <div className="form-group">
            <label htmlFor="tan">TAN Number</label>
            <input
              type="text"
              id="tan"
              name="tan"
              value={formData.tan}
              onChange={handleChange}
              className={validationErrors.tan ? 'error' : ''}
              placeholder="AAAA12345B"
              maxLength="10"
            />
            {validationErrors.tan && <div className="field-error">{validationErrors.tan}</div>}
          </div>

          <div className="form-group">
            <label htmlFor="cin">CIN Number</label>
            <input
              type="text"
              id="cin"
              name="cin"
              value={formData.cin}
              onChange={handleChange}
              className={validationErrors.cin ? 'error' : ''}
              placeholder="L12345AB1234567890123"
              maxLength="21"
            />
            {validationErrors.cin && <div className="field-error">{validationErrors.cin}</div>}
          </div>
        </div>

        {/* Bank Information Section */}
        <div className="form-section">
          <h3>Bank Information</h3>

          <div className="form-group">
            <label htmlFor="bankName">Bank Name *</label>
            <input
              type="text"
              id="bankName"
              name="bankName"
              value={formData.bankName}
              onChange={handleChange}
              placeholder="Enter bank name or select from list"
              list="bankList"
              required
            />
            <datalist id="bankList">
              {commonBanks.map((bank, index) => (
                <option key={index} value={bank} />
              ))}
            </datalist>
            {isHdfcBank && (
              <div className="hdfc-notice">
                <span className="hdfc-icon">🏦</span>
                <span>HDFC Bank selected - Beneficiary Code will be required below</span>
              </div>
            )}
          </div>

          <div className="form-group">
            <label htmlFor="bankAcc">Bank Account Number *</label>
            <input
              type="text"
              id="bankAcc"
              name="bankAcc"
              value={formData.bankAcc}
              onChange={handleChange}
              className={validationErrors.bankAcc ? 'error' : ''}
              placeholder="Enter account number"
              required
            />
            {validationErrors.bankAcc && <div className="field-error">{validationErrors.bankAcc}</div>}
          </div>

          <div className="form-group">
            <label htmlFor="bankAccType">Bank Account Type</label>
            <select
              id="bankAccType"
              name="bankAccType"
              value={formData.bankAccType || ''}
              onChange={handleChange}
            >
              <option value="">Select account type</option>
              <option value="SAVINGS">Savings</option>
              <option value="CURRENT">Current</option>
              <option value="CASH CREDIT">Cash Credit</option>
              <option value="OVERDRAFT">Overdraft</option>
            </select>
          </div>

          {isHdfcBank && (
            <div className="form-group">
              <label htmlFor="beneficiaryCode">
                Beneficiary Code <span className="required">*</span>
                <span className="field-hint">(Required for HDFC Bank)</span>
              </label>
              <input
                type="text"
                id="beneficiaryCode"
                name="beneficiaryCode"
                value={formData.beneficiaryCode}
                onChange={handleChange}
                className={validationErrors.beneficiaryCode ? 'error' : ''}
                placeholder="Enter 6-12 character beneficiary code"
                maxLength="12"
              />
              {validationErrors.beneficiaryCode && <div className="field-error">{validationErrors.beneficiaryCode}</div>}
            </div>
          )}

          <div className="form-group">
            <label htmlFor="branchAdd">Branch Address</label>
            <textarea
              id="branchAdd"
              name="branchAdd"
              value={formData.branchAdd}
              onChange={handleChange}
              placeholder="Enter branch address"
              rows="2"
            />
          </div>

          <div className="form-group">
            <label htmlFor="ifsc">IFSC Code *</label>
            <input
              type="text"
              id="ifsc"
              name="ifsc"
              value={formData.ifsc}
              onChange={handleChange}
              className={validationErrors.ifsc ? 'error' : ''}
              placeholder="ABCD0123456"
              maxLength="11"
              required
            />
            {validationErrors.ifsc && <div className="field-error">{validationErrors.ifsc}</div>}
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
        </div>

        {/* Services Section */}
        <div className="form-section">
          <div className="services-header">
            <h3>Services & TDS Rates</h3>
            {editingVendor && !selectedServices.some(service => service.usedInInvoices) && (
              <button
                type="button"
                onClick={handleServiceEditorToggle}
                className="toggle-service-editor-btn"
              >
                {showServiceEditor ? '🔒 Lock Services' : '✏️ Edit Services'}
              </button>
            )}
          </div>

          {selectedServices.map((service, index) => (
            <div key={index} className={`service-row ${service.usedInInvoices ? 'used-in-invoices' : ''}`}>
              <div className="form-group">
                <label>
                  Service Type
                  {service.usedInInvoices && (
                    <span className="invoice-badge">🔒 Used in Invoices</span>
                  )}
                </label>
                {service.usedInInvoices ? (
                  <div className="locked-service-type">
                    {getServiceTypeName(service.serviceTypeId)}
                    <div className="service-warning">
                      ⚠️ This service is used in invoices. Service type cannot be changed, only TDS rate can be updated.
                    </div>
                  </div>
                ) : !showServiceEditor ? (
                  <div className="locked-service-type">
                    {getServiceTypeName(service.serviceTypeId)}
                    <div className="service-hint">
                      ℹ️ Click "Edit Services" to modify service type and TDS rate.
                    </div>
                  </div>
                ) : (
                  <select
                    value={service.serviceTypeId}
                    onChange={(e) => handleServiceChange(index, 'serviceTypeId', e.target.value)}
                    required
                  >
                    <option value="">Select a service type</option>
                    {serviceTypeOptions.map((type) => (
                      <option key={type.id} value={type.id}>
                        {type.name}
                      </option>
                    ))}
                  </select>
                )}
              </div>

              <div className="form-group">
                <label>TDS Rate (%)</label>
                {!showServiceEditor ? (
                  <div className="locked-tds-rate">
                    {service.tdsRate || '0.00'}%
                  </div>
                ) : (
                  <input
                    type="number"
                    value={service.tdsRate}
                    onChange={(e) => handleServiceChange(index, 'tdsRate', e.target.value)}
                    placeholder="0.00"
                    step="0.01"
                    min="0"
                    max="100"
                    required
                  />
                )}
              </div>

              {selectedServices.length > 1 && !service.usedInInvoices && showServiceEditor && (
                <button
                  type="button"
                  onClick={() => removeService(index)}
                  className="remove-service-btn"
                  title="Remove service"
                >
                  Remove
                </button>
              )}
            </div>
          ))}

          {!selectedServices.some(service => service.usedInInvoices) && showServiceEditor && (
            <button type="button" onClick={addService} className="add-service-btn">
              + Add Another Service
            </button>
          )}

          {validationErrors.services && (
            <div className="field-error">{validationErrors.services}</div>
          )}
        </div>

        <div className="form-actions">
          <button type="submit" className="submit-btn" disabled={loading}>
            {loading ? (editingVendor ? 'Updating Vendor...' : 'Creating Vendor...') : (editingVendor ? 'Update Vendor' : 'Create Vendor')}
          </button>
        </div>
      </form>
    </div>
  );
};

export default VendorForm;