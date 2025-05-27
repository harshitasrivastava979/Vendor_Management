import React, { useState, useEffect } from 'react';
import { vendorAPI } from '../services/api';

const VendorList = ({ refreshTrigger }) => {
  const [vendors, setVendors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const fetchVendors = async () => {
    try {
      setLoading(true);
      const response = await vendorAPI.getAllVendors();
      setVendors(response.data);
      setError('');
    } catch (err) {
      setError('Failed to fetch vendors');
      console.error('Error fetching vendors:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchVendors();
  }, [refreshTrigger]);

  if (loading) return <div className="loading">Loading vendors...</div>;
  if (error) return <div className="error-message">{error}</div>;

  return (
    <div className="vendor-list">
      <h2>Vendors ({vendors.length})</h2>

      {vendors.length === 0 ? (
        <p className="no-vendors">No vendors found. Add your first vendor above!</p>
      ) : (
        <div className="vendors-table">
          <table>
            <thead>
              <tr>
                <th>Name</th>
                <th>Contact</th>
                <th>GST/TAN/CIN</th>
                <th>Bank Account</th>
                <th>IFSC</th>
                <th>NEFT</th>
                <th>Created</th>
              </tr>
            </thead>
            <tbody>
              {vendors.map((vendor) => (
                <tr key={vendor.id}>
                  <td>
                    <div className="vendor-name">{vendor.name}</div>
                    <div className="vendor-address">{vendor.address}</div>
                  </td>
                  <td>{vendor.contact || '-'}</td>
                  <td>{vendor.gstTanCin || '-'}</td>
                  <td>{vendor.bankAcc || '-'}</td>
                  <td>{vendor.ifsc || '-'}</td>
                  <td>
                    <span className={`status ${vendor.neftEnabled ? 'enabled' : 'disabled'}`}>
                      {vendor.neftEnabled ? 'Yes' : 'No'}
                    </span>
                  </td>
                  <td>{new Date(vendor.createdAt).toLocaleDateString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default VendorList;