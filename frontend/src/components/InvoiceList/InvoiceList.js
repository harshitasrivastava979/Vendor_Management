import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { vendorAPI } from '../../services/api.js';
import './InvoiceList.css';

const InvoiceList = () => {
  const { vendorServiceId } = useParams();
  const navigate = useNavigate();
  const [invoices, setInvoices] = useState([]);
  const [filteredInvoices, setFilteredInvoices] = useState([]);
  const [vendorService, setVendorService] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [exportLoading, setExportLoading] = useState(false);
  const [deletingInvoiceId, setDeletingInvoiceId] = useState(null); // Track which invoice is being deleted

  const [dateFilter, setDateFilter] = useState({
    startDate: '',
    endDate: '',
    isActive: false
  });

  const [createdAtFilter, setCreatedAtFilter] = useState({
    startDate: '',
    endDate: '',
    isActive: false
  });

  // Add state to track which filter is active
  const [activeFilter, setActiveFilter] = useState('none'); // 'none', 'transaction', 'created'

  useEffect(() => {
    if (!vendorServiceId) {
      setError('Invalid vendor service ID');
      setLoading(false);
      return;
    }
    fetchData();
  }, [vendorServiceId]);

  useEffect(() => {
    applyActiveFilter();
  }, [dateFilter, createdAtFilter, invoices, activeFilter]);

  const fetchData = async () => {
    try {
      setLoading(true);
      setError('');
      const [vendorServiceData, invoicesData] = await Promise.all([
        vendorAPI.getVendorServiceById(vendorServiceId),
        vendorAPI.getInvoicesByVendorService(vendorServiceId)
      ]);
      setVendorService(vendorServiceData);
      setInvoices(invoicesData);
      setFilteredInvoices(invoicesData);
    } catch (err) {
      console.error('Error fetching data:', err);
      setError('Failed to fetch invoice data');
    } finally {
      setLoading(false);
    }
  };

  const applyActiveFilter = () => {
    if (activeFilter === 'transaction' && dateFilter.isActive) {
      applyTransactionDateFilter();
    } else if (activeFilter === 'created' && createdAtFilter.isActive) {
      applyCreatedAtFilter();
    } else {
      setFilteredInvoices(invoices);
    }
  };

  const applyTransactionDateFilter = () => {
    if (!dateFilter.startDate && !dateFilter.endDate) {
      setFilteredInvoices(invoices);
      return;
    }

    const filtered = invoices.filter(invoice => {
      const transactionDate = new Date(invoice.transactionDate);
      const startDate = dateFilter.startDate ? new Date(dateFilter.startDate) : null;
      const endDate = dateFilter.endDate ? new Date(dateFilter.endDate) : null;

      if (startDate && endDate) {
        return transactionDate >= startDate && transactionDate <= endDate;
      } else if (startDate) {
        return transactionDate >= startDate;
      } else if (endDate) {
        return transactionDate <= endDate;
      }
      return true;
    });

    setFilteredInvoices(filtered);
  };

  const applyCreatedAtFilter = async () => {
    if (!createdAtFilter.startDate || !createdAtFilter.endDate) {
      setFilteredInvoices(invoices);
      return;
    }

    try {
      setLoading(true);
      const filteredInvoices = await vendorAPI.getInvoicesByVendorServiceWithCreatedAtDateRange(
        vendorServiceId,
        createdAtFilter.startDate,
        createdAtFilter.endDate
      );
      setFilteredInvoices(filteredInvoices);
    } catch (err) {
      console.error('Error applying created at filter:', err);
      setError('Failed to filter by created date');
      setFilteredInvoices(invoices);
    } finally {
      setLoading(false);
    }
  };

  const handleDateFilterChange = (field, value) => {
    setDateFilter(prev => ({
      ...prev,
      [field]: value,
      isActive: !!(value || prev[field === 'startDate' ? 'endDate' : 'startDate'])
    }));

    // Clear created at filter when transaction date filter is used
    if (value) {
      setCreatedAtFilter(prev => ({ ...prev, isActive: false }));
      setActiveFilter('transaction');
    }
  };

  const handleCreatedAtFilterChange = (field, value) => {
    setCreatedAtFilter(prev => ({
      ...prev,
      [field]: value,
      isActive: !!(value || prev[field === 'startDate' ? 'endDate' : 'startDate'])
    }));

    // Clear transaction date filter when created at filter is used
    if (value) {
      setDateFilter(prev => ({ ...prev, isActive: false }));
      setActiveFilter('created');
    }
  };

  const clearDateFilter = () => {
    setDateFilter({
      startDate: '',
      endDate: '',
      isActive: false
    });
    setActiveFilter('none');
  };

  const clearCreatedAtFilter = () => {
    setCreatedAtFilter({
      startDate: '',
      endDate: '',
      isActive: false
    });
    setActiveFilter('none');
  };

  const handleExportExcel = async () => {
    try {
      setExportLoading(true);
      const blob = await vendorAPI.exportInvoicesToExcel(
        vendorServiceId,
        dateFilter.startDate,
        dateFilter.endDate
      );
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'transaction_invoices.xlsx');
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
    } catch (err) {
      console.error('Export error:', err);
      setError('Failed to export Excel file');
    } finally {
      setExportLoading(false);
    }
  };

  const handleExportCsv = async () => {
    try {
      setExportLoading(true);
      const blob = await vendorAPI.exportInvoicesToCsv(
        vendorServiceId,
        dateFilter.startDate,
        dateFilter.endDate
      );
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'transaction_invoices.csv');
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
    } catch (err) {
      console.error('Export error:', err);
      setError('Failed to export CSV file');
    } finally {
      setExportLoading(false);
    }
  };

  const handleExportCreatedAtExcel = async () => {
    if (!createdAtFilter.startDate || !createdAtFilter.endDate) {
      setError('Please select both start and end dates for Created At export');
      return;
    }

    try {
      setExportLoading(true);
      const blob = await vendorAPI.exportCreatedInvoicesToExcel(
        vendorServiceId,
        createdAtFilter.startDate,
        createdAtFilter.endDate
      );
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'created_invoices.xlsx');
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
    } catch (err) {
      console.error('CreatedAt Excel Export Error:', err);
      setError('Failed to export CreatedAt Excel file');
    } finally {
      setExportLoading(false);
    }
  };

  const handleExportCreatedAtCsv = async () => {
    if (!createdAtFilter.startDate || !createdAtFilter.endDate) {
      setError('Please select both start and end dates for Created At export');
      return;
    }

    try {
      setExportLoading(true);
      const blob = await vendorAPI.exportCreatedInvoicesToCsv(
        vendorServiceId,
        createdAtFilter.startDate,
        createdAtFilter.endDate
      );
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'created_invoices.csv');
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
    } catch (err) {
      console.error('CreatedAt CSV Export Error:', err);
      setError('Failed to export CreatedAt CSV file');
    } finally {
      setExportLoading(false);
    }
  };

  const handleCreateNewInvoice = () => {
    navigate(`/create-invoice/${vendorServiceId}`);
  };

  const handleDeleteInvoice = async (invoiceId) => {
    if (!window.confirm('Are you sure you want to delete this invoice?')) return;
    setDeletingInvoiceId(invoiceId);
    setError('');
    try {
      await vendorAPI.deleteInvoice(invoiceId);
      setInvoices(prev => prev.filter(inv => inv.id !== invoiceId));
      setFilteredInvoices(prev => prev.filter(inv => inv.id !== invoiceId));
    } catch (err) {
      setError(err.message || 'Failed to delete invoice');
    } finally {
      setDeletingInvoiceId(null);
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    return new Date(dateString).toLocaleDateString('en-IN');
  };

  const formatCurrency = (amount) => {
    if (!amount) return '₹0.00';
    return `₹${parseFloat(amount).toFixed(2)}`;
  };

  const handlePreview = async (invoiceId) => {
    try {
      const res = await vendorAPI.previewInvoiceBill(invoiceId);
      window.open(res.data, '_blank');
    } catch (err) {
      setError('Failed to preview bill');
    }
  };

  const handleDownload = async (invoiceId) => {
    try {
      const res = await vendorAPI.downloadInvoiceBill(invoiceId);
      // Try to get the filename from the Content-Disposition header
      let filename = 'bill';
      const disposition = res.headers['content-disposition'];
      if (disposition && disposition.indexOf('filename=') !== -1) {
        filename = disposition.split('filename=')[1].replace(/['"]/g, '');
      }
      // Get the content type from the response
      const contentType = res.headers['content-type'] || 'application/octet-stream';
      const blob = new Blob([res.data], { type: contentType });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', filename);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      setError('Failed to download bill');
    }
  };

  if (loading) {
    return <div className="loading-container"><div className="loading-spinner" /><p>Loading invoices...</p></div>;
  }

  if (error) {
    return (
      <div className="error-container">
        <div className="error-message">
          <h3>Error Loading Invoices</h3>
          <p>{error}</p>
          <button onClick={() => {
            setError('');
            fetchData();
          }}>Try Again</button>
        </div>
      </div>
    );
  }

  return (
    <div className="invoice-list-container">
      <div className="invoice-list-header">
        <div className="header-info">
          <h2>Invoice List</h2>
          <div className="vendor-service-info">
            <p><strong>Vendor:</strong> {vendorService.vendorName}</p>
            <p><strong>Service:</strong> {vendorService.serviceType?.name}</p>
            <p><strong>TDS Rate:</strong> {vendorService.tdsRate}%</p>
          </div>
        </div>
        <div className="header-actions">
          <button className="btn-create-invoice" onClick={handleCreateNewInvoice}>📄 Create New Invoice</button>
          <button className="btn-back" onClick={() => navigate(`/vendor/${vendorService.vendorId}/services`)}>← Back to Services</button>
        </div>
      </div>

      {/* Transaction Date Filter */}
      <div className={`filter-section ${activeFilter === 'transaction' ? 'active' : ''}`}>
        <div className="filter-header">
          <h3>📅 Filter by Transaction Date</h3>
          {dateFilter.isActive && (
            <button className="btn-clear-filter" onClick={clearDateFilter}>Clear Filter</button>
          )}
        </div>
        <div className="date-filter-controls">
          <div className="date-input-group">
            <label>From Date:</label>
            <input
              type="date"
              value={dateFilter.startDate}
              onChange={(e) => handleDateFilterChange('startDate', e.target.value)}
              className="date-input"
              disabled={activeFilter === 'created'}
            />
          </div>
          <div className="date-input-group">
            <label>To Date:</label>
            <input
              type="date"
              value={dateFilter.endDate}
              onChange={(e) => handleDateFilterChange('endDate', e.target.value)}
              className="date-input"
              disabled={activeFilter === 'created'}
            />
          </div>
          <div className="filter-actions">
            <button
              className="btn-export-excel"
              onClick={handleExportExcel}
              disabled={exportLoading || activeFilter === 'created'}
            >
              {exportLoading ? '⏳ Exporting...' : '📊 Export to Excel'}
            </button>
            <button
              className="btn-export-excel"
              onClick={handleExportCsv}
              disabled={exportLoading || activeFilter === 'created'}
            >
              {exportLoading ? '⏳ Exporting...' : '📁 Export to CSV'}
            </button>
          </div>
        </div>
      </div>

      {/* Created At Filter */}
      <div className={`filter-section ${activeFilter === 'created' ? 'active' : ''}`}>
        <div className="filter-header">
          <h3>🕓 Filter by Created Date</h3>
          {createdAtFilter.isActive && (
            <button className="btn-clear-filter" onClick={clearCreatedAtFilter}>Clear Filter</button>
          )}
        </div>
        <div className="date-filter-controls">
          <div className="date-input-group">
            <label>From Created At:</label>
            <input
              type="date"
              value={createdAtFilter.startDate}
              onChange={(e) => handleCreatedAtFilterChange('startDate', e.target.value)}
              className="date-input"
              disabled={activeFilter === 'transaction'}
            />
          </div>
          <div className="date-input-group">
            <label>To Created At:</label>
            <input
              type="date"
              value={createdAtFilter.endDate}
              onChange={(e) => handleCreatedAtFilterChange('endDate', e.target.value)}
              className="date-input"
              disabled={activeFilter === 'transaction'}
            />
          </div>
          <div className="filter-actions">
            <button
              className="btn-export-excel"
              onClick={handleExportCreatedAtExcel}
              disabled={exportLoading || activeFilter === 'transaction' || !createdAtFilter.startDate || !createdAtFilter.endDate}
            >
              {exportLoading ? '⏳ Exporting...' : '📊 Export to Excel'}
            </button>
            <button
              className="btn-export-excel"
              onClick={handleExportCreatedAtCsv}
              disabled={exportLoading || activeFilter === 'transaction' || !createdAtFilter.startDate || !createdAtFilter.endDate}
            >
              {exportLoading ? '⏳ Exporting...' : '📁 Export to CSV'}
            </button>
          </div>
        </div>
      </div>

      {/* Active Filter Status */}
      {activeFilter !== 'none' && (
        <div className="filter-status">
          {activeFilter === 'transaction' && (
            <span>🔍 Filtering by Transaction Date: {filteredInvoices.length} of {invoices.length} invoices</span>
          )}
          {activeFilter === 'created' && (
            <span>🔍 Filtering by Created Date: {filteredInvoices.length} of {invoices.length} invoices</span>
          )}
        </div>
      )}

      {/* Table */}
      {filteredInvoices.length === 0 ? (
        <div className="no-invoices">
          <h3>No Invoices Found</h3>
          <p>{activeFilter !== 'none' ? 'No invoices match the selected filter criteria.' : 'Create one or apply filters.'}</p>
        </div>
      ) : (
        <div className="invoices-table-container">
          <table className="invoices-table">
            <thead>
              <tr>
                <th>Invoice #</th>
                <th>Transaction Date</th>
                <th>Invoice Month</th>
                <th>Base Amount</th>
                <th>GST Amount</th>
                <th>TDS Amount</th>
                <th>Final Amount</th>
                <th>Payment Method</th>
                <th>Status</th>
                <th>Bill File</th>
                <th>Action</th> {/* Add Action column */}
              </tr>
            </thead>
            <tbody>
              {filteredInvoices.map((invoice, index) => (
                <tr key={invoice.id || index}>
                  <td>{invoice.invoiceNumber || 'N/A'}</td>
                  <td>{formatDate(invoice.transactionDate)}</td>
                  <td>{invoice.invoiceMonth || 'N/A'}</td>
                  <td>{formatCurrency(invoice.amountExclusive)}</td>
                  <td>{formatCurrency(invoice.gstAmount)}</td>
                  <td>{formatCurrency(invoice.tdsAmount)}</td>
                  <td className="final-amount">{formatCurrency(invoice.finalAmount)}</td>
                  <td>
                    <span className={`payment-method-badge ${invoice.paymentMethod?.toLowerCase() || 'neft'}`}>
                      {invoice.paymentMethod || 'NEFT'}
                    </span>
                  </td>
                  <td>
                    <span className={`status-badge ${invoice.invoiceType?.toLowerCase() || 'standard'}`}>
                      {invoice.invoiceType || 'STANDARD'}
                    </span>
                  </td>
                  <td>
                    {invoice.billFileKey ? (
                      <>
                        <button
                          className="btn-preview"
                          onClick={() => handlePreview(invoice.id)}
                          title="Preview Bill"
                        >👁️ Preview</button>
                        <button
                          className="btn-download"
                          onClick={() => handleDownload(invoice.id)}
                          title="Download Bill"
                        >⬇️ Download</button>
                      </>
                    ) : (
                      <span>No Bill</span>
                    )}
                  </td>
                  <td>
                    <button
                      className="btn-delete-invoice"
                      onClick={() => handleDeleteInvoice(invoice.id)}
                      disabled={deletingInvoiceId === invoice.id}
                      title="Delete this invoice"
                    >
                      {deletingInvoiceId === invoice.id ? 'Deleting...' : '🗑️ Delete'}
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default InvoiceList;
