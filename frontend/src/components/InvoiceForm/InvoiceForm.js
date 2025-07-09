// ... existing imports
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
  const [billFile, setBillFile] = useState(null);

  const [formData, setFormData] = useState({
    invoiceNumber: '',
    transactionDate: new Date().toISOString().slice(0, 16),
    invoiceMonth: new Date().toISOString().slice(0, 7),
    amount: '',
    gstPercent: '',
    paymentMethod: 'NEFT',
    description: '',
    paymentStatus: 'PENDING',
    invoiceType: 'AVAILABLE'
  });

  const [calculatedAmounts, setCalculatedAmounts] = useState({
    gstAmount: 0,
    tdsAmount: 0,
    totalPayable: 0
  });

  useEffect(() => {
    if (!vendorServiceId) {
      setError('Invalid vendor service ID');
      setLoading(false);
      return;
    }
    fetchVendorService();
  }, [vendorServiceId]);

  useEffect(() => {
    const amount = parseFloat(formData.amount) || 0;
    const gstPercent = parseFloat(formData.gstPercent) || 0;
    const tdsPercent = vendorService?.tdsRate || 0;

    const gstAmount = (amount * gstPercent) / 100;
    const tdsAmount = (amount * tdsPercent) / 100;
    const finalPayable = amount - tdsAmount + gstAmount;

    setCalculatedAmounts({
      gstAmount,
      tdsAmount,
      totalPayable: finalPayable
    });
  }, [formData.amount, formData.gstPercent, vendorService?.tdsRate]);

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
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleFileChange = (e) => {
    setBillFile(e.target.files[0]);
  };
  const handleCancel = () => {
    if (vendorService && (vendorService.vendorId || vendorService.vendor_id || vendorService.id)) {
      navigate(`/vendor/${vendorService.vendorId || vendorService.vendor_id || vendorService.id}/services`);
    } else {
      navigate('/');
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    if (formData.invoiceNumber.length > 20) {
      setError('Invoice Number must be 20 characters or less.');
      return;
    }
    try {
      const payload = {
        vendorServiceId,
        invoiceNumber: formData.invoiceNumber,
        transactionDate: formData.transactionDate.split('T')[0],
        invoiceMonth: formData.invoiceMonth,
        invoiceType: formData.invoiceType,
        amountExclusive: parseFloat(formData.amount),
        gstPercent: parseFloat(formData.gstPercent),
        tdsPercent: vendorService?.tdsRate || 0,
        isGstInclusive: false,
        gstAmount: calculatedAmounts.gstAmount,
        tdsAmount: calculatedAmounts.tdsAmount,
        amountInclusive: calculatedAmounts.totalPayable,
        finalAmount: calculatedAmounts.totalPayable,
        paymentMethod: formData.paymentMethod,
        paymentStatus: formData.paymentStatus
      };
      const formDataToSend = new FormData();
      formDataToSend.append('dto', JSON.stringify(payload));
      if (billFile) {
        formDataToSend.append('billFile', billFile);
      }
      await vendorAPI.createInvoice(formDataToSend, true);
      navigate(`/invoices/${vendorServiceId}`);
    } catch (err) {
      setError('Failed to create invoice');
    }
  };

  if (loading) return <div className="loading">Loading...</div>;
  if (error) return <div className="error">{error}</div>;
  if (!vendorService || !vendorService.vendorName || !vendorService.serviceType) {
    return <div className="error">Vendor service not found</div>;
  }

  return (
    <div className="invoice-form-container">
      <div className="vendor-info">
        <h2>Create Invoice</h2>
        <div className="info-grid">
          <div><strong>Vendor:</strong> {vendorService.vendorName}</div>
          <div><strong>Service:</strong> {vendorService.serviceType.name}</div>
          <div><strong>TDS Rate:</strong> {vendorService.tdsRate}%</div>
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
            maxLength={20}
            placeholder="Enter invoice number"
          />
        </div>

        <div className="form-row">
          <div className="form-group">
            <label htmlFor="transactionDate">Transaction Date & Time *</label>
            <input
              type="datetime-local"
              id="transactionDate"
              name="transactionDate"
              value={formData.transactionDate}
              onChange={handleChange}
              required
            />
          </div>
        </div>

        <div className="form-group">
          <label htmlFor="invoiceMonth">Invoice Month *</label>
          <input
            type="month"
            id="invoiceMonth"
            name="invoiceMonth"
            value={formData.invoiceMonth}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="invoiceType">Invoice Type *</label>
          <select
            id="invoiceType"
            name="invoiceType"
            value={formData.invoiceType}
            onChange={handleChange}
            required
          >
            <option value="AVAILABLE">Available</option>
            <option value="NOT_AVAILABLE">Not Available</option>
          </select>
        </div>

        <div className="form-group">
          <label htmlFor="gstPercent">GST Percentage *</label>
          <input
            type="number"
            id="gstPercent"
            name="gstPercent"
            value={formData.gstPercent}
            onChange={handleChange}
            required
            min="0"
            max="100"
            step="0.01"
            placeholder="Enter GST percentage"
          />
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

        <div className="calculated-amounts">
          <h3>Calculated Amounts</h3>
          <div className="amount-grid">
            <div className="amount-item">
              <label>Base Amount:</label>
              <span>₹{parseFloat(formData.amount) || 0}</span>
            </div>
            <div className="amount-item">
              <label>GST Amount ({formData.gstPercent || 0}%):</label>
              <span>₹{calculatedAmounts.gstAmount.toFixed(2)}</span>
            </div>
            <div className="amount-item">
              <label>TDS Amount ({vendorService?.tdsRate || 0}%):</label>
              <span className="tds-deduction">-₹{calculatedAmounts.tdsAmount.toFixed(2)}</span>
            </div>
            <div className="amount-item total">
              <label>Final Payable:</label>
              <span>₹{calculatedAmounts.totalPayable.toFixed(2)}</span>
            </div>
          </div>
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

        <div className="form-group">
          <label htmlFor="paymentMethod">Payment Method *</label>
          <select
            id="paymentMethod"
            name="paymentMethod"
            value={formData.paymentMethod}
            onChange={handleChange}
            required
          >
            <option value="NEFT">NEFT (National Electronic Funds Transfer)</option>
            <option value="RTGS">RTGS (Real Time Gross Settlement)</option>
            <option value="IFT">IFT (Internal Fund Transfer - HDFC Bank)</option>
          </select>
          <div className="payment-method-info">
            <small>
              <strong>NEFT:</strong> For amounts up to ₹2 lakhs, processed in batches<br />
              <strong>RTGS:</strong> For amounts ₹2 lakhs and above, real-time processing<br />
              <strong>IFT:</strong> Internal transfer within HDFC Bank accounts (instant)
            </small>
          </div>
        </div>

        <div className="form-group">
          <label htmlFor="billFile">Upload Bill (PDF/Image)</label>
          <input
            type="file"
            id="billFile"
            name="billFile"
            accept="application/pdf,image/*"
            onChange={handleFileChange}
          />
        </div>

        <div className="form-actions">

          <button type="submit" className="btn-submit">
            Create Invoice
          </button>
        </div>
      </form>
    </div>
  );
};

export default InvoiceForm;
