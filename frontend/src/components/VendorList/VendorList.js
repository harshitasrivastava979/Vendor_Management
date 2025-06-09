import React, { useState, useEffect } from 'react';
import { vendorAPI } from '../../services/api.js';
import '../VendorServices/VendorServices.css';

const VendorList = ({ onNotification, refreshTrigger, onVendorSelected }) => {
  const [vendors, setVendors] = useState([]);

  // Fetch vendors from the backend
  const fetchVendors = async () => {
    try {
      const data = await vendorAPI.getAllVendors();
      setVendors(data);
    } catch (error) {
      console.error('Error fetching vendors:', error);
      if (onNotification) {
        onNotification('Error', 'Failed to fetch vendors', 'error');
      }
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

  return (
    <div className="vendor-list">
      <h2>Vendor List</h2>
      {vendors.length === 0 ? (
        <p>No vendors available.</p>
      ) : (
        vendors.map((vendor) => (
          <div key={vendor.id} className="vendor-card">
            <h3>{vendor.name}</h3>
            <p>{vendor.address}</p>
            {/* Optionally allow vendor selection */}
            <button onClick={() => onVendorSelected && onVendorSelected(vendor.id)}>
              Select Vendor
            </button>
            {/* Delete button calls handleDeleteVendor */}
            <button
              onClick={() => handleDeleteVendor(vendor.id, vendor.name)}
              className="btn-delete"
            >
              Delete Vendor
            </button>
          </div>
        ))
      )}
    </div>
  );
};

export default VendorList;