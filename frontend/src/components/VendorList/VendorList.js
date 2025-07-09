import React, { useState, useEffect } from 'react';
import { vendorAPI } from '../../services/api';
import './VendorList.css';

const VendorList = ({ onNotification, refreshTrigger, onVendorSelected, onEditVendor }) => {
  const [vendors, setVendors] = useState([]);
  const [loading, setLoading] = useState(true);

  // Fetch vendors from the backend
  const fetchVendors = async () => {
    try {
      setLoading(true);
      const data = await vendorAPI.getAllVendors();
      setVendors(data);
    } catch (error) {
      console.error('Error fetching vendors:', error);
      if (onNotification) {
        onNotification('Error', 'Failed to fetch vendors', 'error');
      }
    } finally {
      setLoading(false);
    }
  };

  // Fetch vendors on mount or whenever refreshTrigger changes
  useEffect(() => {
    fetchVendors();
  }, [refreshTrigger]);

  // Delete a vendor after user confirmation
  const handleDeleteVendor = async (vendorId, vendorName) => {
    if (!window.confirm(`Are you sure you want to delete "${vendorName}"?`)) {
      return;
    }

    try {
      // Call the vendorAPI delete method (make sure your backend supports DELETE /vendors/:id)
      await vendorAPI.deleteVendor(vendorId);

      // Update local state: remove the deleted vendor from the vendors array
      setVendors(prevVendors => prevVendors.filter(vendor => vendor.id !== vendorId));

      // Optionally, show a notification upon successful deletion
      if (onNotification) {
        onNotification('Success', `Vendor "${vendorName}" deleted successfully!`, 'success');
      }
    } catch (err) {
      console.error('Error deleting vendor:', err);
      if (onNotification) {
        onNotification('Error', 'Failed to delete vendor. Please try again.', 'error');
      }
    }
  };

  if (loading) {
    return (
      <div className="vendor-list">
        <h2>Vendor List</h2>
        <div className="loading-message">Loading vendors...</div>
      </div>
    );
  }

  return (
    <div className="vendor-list">
      <div className="vendor-list-header">
        <h2>Vendor List</h2>
        <span className="vendor-count">{vendors.length} vendor{vendors.length !== 1 ? 's' : ''}</span>
      </div>
      
      {vendors.length === 0 ? (
        <div className="empty-state">
          <p>No vendors available.</p>
          <p className="empty-state-hint">Add a vendor using the form on the left to get started.</p>
        </div>
      ) : (
        <div className="vendor-grid">
          {vendors.map((vendor) => (
            <div key={vendor.id} className="vendor-card">
              <div className="vendor-info">
                <h3 className="vendor-name">{vendor.name}</h3>
                <p className="vendor-address">{vendor.address}</p>
                {vendor.contact && (
                  <p className="vendor-contact">📞 {vendor.contact}</p>
                )}
                {vendor.gst && (
                  <p className="vendor-gst">🏢 GST: {vendor.gst}</p>
                )}
              </div>
              
              <div className="vendor-actions">
                <button 
                  className="btn-select"
                  onClick={() => onVendorSelected && onVendorSelected(vendor.id)}
                  title="Select this vendor to view services and create invoices"
                >
                  📋 Select Vendor
                </button>
                

                <button
                  className="btn-edit"
                  onClick={() => onEditVendor && onEditVendor(vendor)}
                  title="Edit this vendor"
                >
                  ✏️ Edit
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default VendorList;