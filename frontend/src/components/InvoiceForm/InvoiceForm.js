import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { vendorAPI } from '../../services/api.js';
import './InvoiceForm.css';

const InvoiceForm = () => {
  const { vendorServiceId } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [vendorService, setVendorService] = useState(null);
  
  const [formData, setFormData] = useState({
    invoiceNumber: '',
    invoiceDate: new Date().toISOString().split('T')[0],
    amount: '',
    description: '',
    paymentDueDate: '',
    paymentStatus: 'PENDING'
  });

  useEffect(() => {
    fetchVendorService();
  }, [vendorServiceId]);

  const fetchVendorService = async () => {
    try {
      setLoading(true);
      const data = await vendorAPI.getVendorServiceById(vendorServiceId);
      setVendorService(data);
    } catch (err) {
      setError('Failed to fetch vendor service details');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const payload = {
        ...formData,
        vendorServiceId: vendorServiceId,
        amount: parseFloat(formData.amount)
      };

      await vendorAPI.createInvoice(payload);
      navigate(`/vendor/${vendorService.vendor.id}/services`);
    } catch (err) {
      setError('Failed to create invoice');
    }
  };

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  if (error) {
    return <div className="error">{error}</div>;
  }

  if (!vendorService) {
    return <div className="error">Vendor service not found</div>;
  }

  return (
    <div className="invoice-form-container">
      <div className="vendor-info">
        <h2>Create Invoice</h2>
        <div className="info-grid">
          <div>
            <strong>Vendor:</strong> {vendorService.vendor.name}
          </div>
          <div>
            <strong>Service:</strong> {vendorService.serviceType.name}
          </div>
          <div>
            <strong>TDS Rate:</strong> {vendorService.tdsRate}%
          </div>
        </div>
      </div>

      <form onSubmit={handleSubmit} className="invoice-form">
        <div className="form-group">
          <label htmlFor="invoiceNumber">Invoice Number *</label>
          <input
            type="text"
            id="invoiceNumber"
            name="invoiceNumber"
            value={formData.invoiceNumber}
            onChange={handleChange}
            required
            placeholder="Enter invoice number"
          />
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="invoiceDate">Invoice Date *</label>
            <input
              type="date"
              id="invoiceDate"
              name="invoiceDate"
              value={formData.invoiceDate}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="paymentDueDate">Payment Due Date *</label>
            <input
              type="date"
              id="paymentDueDate"
              name="paymentDueDate"
              value={formData.paymentDueDate}
              onChange={handleChange}
              required
              min={formData.invoiceDate}
            />
          </div>
        </div>

        <div className="form-group">
          <label htmlFor="amount">Amount (₹) *</label>
          <input
            type="number"
            id="amount"
            name="amount"
            value={formData.amount}
            onChange={handleChange}
            required
            min="0"
            step="0.01"
            placeholder="Enter invoice amount"
          />
        </div>

        <div className="form-group">
          <label htmlFor="description">Description</label>
          <textarea
            id="description"
            name="description"
            value={formData.description}
            onChange={handleChange}
            rows="3"
            placeholder="Enter invoice description"
          />
        </div>

        <div className="form-group">
          <label htmlFor="paymentStatus">Payment Status</label>
          <select
            id="paymentStatus"
            name="paymentStatus"
            value={formData.paymentStatus}
            onChange={handleChange}
          >
            <option value="PENDING">Pending</option>
            <option value="PAID">Paid</option>
            <option value="OVERDUE">Overdue</option>
          </select>
        </div>

        <div className="form-actions">
          <button
            type="button"
            onClick={() => navigate(`/vendor/${vendorService.vendor.id}/services`)}
            className="btn-cancel"
          >
            Cancel
          </button>
          <button type="submit" className="btn-submit">
            Create Invoice
          </button>
        </div>
      </form>
    </div>
  );
};

export default InvoiceForm; 