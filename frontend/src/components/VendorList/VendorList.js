// Update your src/components/VendorList/VendorList.js with this version

import React, { useState, useEffect } from 'react';
import { vendorAPI } from '../../services/api.js';
import './VendorList.css';

const VendorList = ({ refreshTrigger, onNotification }) => {
  const [vendors, setVendors] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [searchTerm, setSearchTerm] = useState('');
  const [filteredVendors, setFilteredVendors] = useState([]);

  useEffect(() => {
    fetchVendors();
  }, [refreshTrigger]);

  useEffect(() => {
    // Filter vendors based on search term
    if (searchTerm.trim() === '') {
      setFilteredVendors(vendors);
    } else {
      const filtered = vendors.filter(vendor =>
        vendor.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        vendor.contact?.includes(searchTerm) ||
        vendor.gstTanCin?.toLowerCase().includes(searchTerm.toLowerCase())
      );
      setFilteredVendors(filtered);
    }
  }, [vendors, searchTerm]);

  const fetchVendors = async () => {
    setLoading(true);
    setError('');

    try {
      console.log('📥 Fetching vendors from Spring Boot...');

      // Call real API
      const vendorData = await vendorAPI.getAllVendors();

      console.log('✅ Vendors fetched successfully:', vendorData);

      // Handle both array response and object with array
      const vendorArray = Array.isArray(vendorData) ? vendorData : vendorData.vendors || [];

      setVendors(vendorArray);

    } catch (err) {
      console.error('❌ Error fetching vendors:', err);
      const errorMessage = err.message || 'Failed to fetch vendors';
      setError(errorMessage);

      if (onNotification) {
        onNotification('Error', errorMessage, 'error');
      }

      // Show mock data if backend is not available
      if (err.message.includes('Backend server is not running')) {
        console.log('📋 Using mock data since backend is offline');
        setMockData();
      }
    } finally {
      setLoading(false);
    }
  };

  const setMockData = () => {
    const mockVendors = [
      {
        id: "mock-1",
        name: "Demo Vendor (Backend Offline)",
        address: "123 Demo Street, Demo City",
        contact: "9876543210",
        gstTanCin: "29ABCDE1234F1Z5",
        bankAcc: "123456789012345",
        ifsc: "HDFC0001234",
        neftEnabled: true,
        createdAt: "2024-01-15T00:00:00",
        updatedAt: "2024-01-15T00:00:00"
      }
    ];
    setVendors(mockVendors);
  };

  const handleEdit = (vendorId) => {
    console.log('✏️ Edit vendor:', vendorId);
    if (onNotification) {
      onNotification('Info', 'Edit functionality will be implemented soon', 'info');
    }
  };

  const handleDelete = async (vendorId, vendorName) => {
    const confirmed = window.confirm(`Are you sure you want to delete "${vendorName}"?\n\nThis action cannot be undone.`);

    if (confirmed) {
      try {
        console.log('🗑️ Deleting vendor:', vendorId);

        // Call real API
        await vendorAPI.deleteVendor(vendorId);

        console.log('✅ Vendor deleted successfully');

        // Remove from local state
        setVendors(vendors.filter(vendor => vendor.id !== vendorId));

        if (onNotification) {
          onNotification('Success', `Vendor "${vendorName}" deleted successfully`, 'success');
        }
      } catch (err) {
        console.error('❌ Error deleting vendor:', err);
        if (onNotification) {
          onNotification('Error', err.message || 'Failed to delete vendor', 'error');
        }
      }
    }
  };

  const handleViewInvoices = (vendorId, vendorName) => {
    console.log('📄 View invoices for vendor:', vendorId);
    if (onNotification) {
      onNotification('Info', `Invoice management for "${vendorName}" coming soon`, 'info');
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return '-';
    try {
      return new Date(dateString).toLocaleDateString('en-IN');
    } catch (e) {
      return dateString;
    }
  };

  if (loading) {
    return (
      <div className="vendor-list">
        <div className="list-header">
          <h2>Vendors</h2>
        </div>
        <div className="loading-container">
          <div className="loading-spinner"></div>
          <p>Loading vendors from Spring Boot...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="vendor-list">
      <div className="list-header">
        <h2>Vendors ({filteredVendors.length})</h2>
        <div className="header-actions">
          <div className="search-container">
            <input
              type="text"
              placeholder="Search vendors..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="search-input"
            />
            <span className="search-icon">🔍</span>
          </div>
          <button
            onClick={fetchVendors}
            className="refresh-btn"
            disabled={loading}
            title="Refresh vendor list"
          >
            🔄
          </button>
        </div>
      </div>

      {error && (
        <div className="alert alert-error">
          ⚠ {error}
          {error.includes('Backend server is not running') && (
            <div className="error-help">
              <p>💡 <strong>To fix this:</strong></p>
              <ol>
                <li>Start your Spring Boot application</li>
                <li>Make sure it's running on port 8080</li>
                <li>Click the refresh button above</li>
              </ol>
            </div>
          )}
        </div>
      )}

      {filteredVendors.length === 0 ? (
        <div className="no-vendors">
          {searchTerm ? (
            <>
              <div className="no-vendors-icon">🔍</div>
              <h3>No vendors found</h3>
              <p>No vendors match your search term "{searchTerm}"</p>
              <button
                className="clear-search-btn"
                onClick={() => setSearchTerm('')}
              >
                Clear Search
              </button>
            </>
          ) : (
            <>
              <div className="no-vendors-icon">📋</div>
              <h3>No vendors yet</h3>
              <p>Add your first vendor using the form on the left!</p>
              {!error && (
                <button
                  className="clear-search-btn"
                  onClick={fetchVendors}
                >
                  Refresh List
                </button>
              )}
            </>
          )}
        </div>
      ) : (
        <div className="vendors-container">
          <div className="vendors-grid">
            {filteredVendors.map((vendor) => (
              <div key={vendor.id} className="vendor-card">
                <div className="vendor-header">
                  <h3 className="vendor-name">{vendor.name}</h3>
                  <div className="vendor-actions">
                    <button
                      className="action-btn edit-btn"
                      onClick={() => handleEdit(vendor.id)}
                      title="Edit vendor"
                    >
                      ✏️
                    </button>
                    <button
                      className="action-btn invoice-btn"
                      onClick={() => handleViewInvoices(vendor.id, vendor.name)}
                      title="View invoices"
                    >
                      📄
                    </button>
                    <button
                      className="action-btn delete-btn"
                      onClick={() => handleDelete(vendor.id, vendor.name)}
                      title="Delete vendor"
                    >
                      🗑️
                    </button>
                  </div>
                </div>

                <div className="vendor-details">
                  {vendor.address && (
                    <div className="detail-row">
                      <span className="detail-label">📍 Address:</span>
                      <span className="detail-value">{vendor.address}</span>
                    </div>
                  )}

                  {vendor.contact && (
                    <div className="detail-row">
                      <span className="detail-label">📞 Contact:</span>
                      <span className="detail-value">{vendor.contact}</span>
                    </div>
                  )}

                  {vendor.gstTanCin && (
                    <div className="detail-row">
                      <span className="detail-label">🏛️ GST:</span>
                      <span className="detail-value gst-number">{vendor.gstTanCin}</span>
                    </div>
                  )}

                  {vendor.bankAcc && (
                    <div className="detail-row">
                      <span className="detail-label">🏦 Bank:</span>
                      <span className="detail-value">
                        {vendor.bankAcc} ({vendor.ifsc})
                      </span>
                    </div>
                  )}

                  <div className="detail-row">
                    <span className="detail-label">💳 NEFT:</span>
                    <span className={`neft-status ${vendor.neftEnabled ? 'enabled' : 'disabled'}`}>
                      {vendor.neftEnabled ? '✅ Enabled' : '❌ Disabled'}
                    </span>
                  </div>

                  <div className="detail-row">
                    <span className="detail-label">📅 Created:</span>
                    <span className="detail-value">{formatDate(vendor.createdAt)}</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default VendorList;