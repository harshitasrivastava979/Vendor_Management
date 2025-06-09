import React, { useState, useEffect } from 'react';
import { vendorAPI } from '../../services/api.js';
import './VendorServices.css';

const VendorServices = ({ vendorId, onCreateInvoice }) => {
  const [vendor, setVendor] = useState(null);
  const [services, setServices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      if (!vendorId) return;

      try {
        setLoading(true);
        setError(null);

        // Fetch both vendor details and services
        const [vendorData, servicesData] = await Promise.all([
          vendorAPI.getVendorById(vendorId),
          vendorAPI.getVendorServices(vendorId)
        ]);

        setVendor(vendorData);
        setServices(servicesData);
      } catch (err) {
        setError(err.message || 'Failed to fetch vendor services');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [vendorId]);

  const handleCreateInvoice = (vendorServiceId, serviceName) => {
    if (onCreateInvoice) {
      onCreateInvoice(vendorServiceId, serviceName, vendor);
    }
  };

  if (loading) {
    return (
      <div className="loading-container">
        <div className="loading-spinner"></div>
        <p>Loading vendor services...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="error-container">
        <div className="error-message">
          <h3>Error Loading Services</h3>
          <p>{error}</p>
          <button onClick={() => window.location.reload()}>Try Again</button>
        </div>
      </div>
    );
  }

  if (!vendor) {
    return <div className="error-message">Vendor not found</div>;
  }

  return (
    <div className="vendor-services">
      {/* Vendor Information Card */}
      <div className="vendor-info">
        <h2>{vendor.name}</h2>
        <div className="vendor-details">
          {vendor.contact && <p><strong>Contact:</strong> {vendor.contact}</p>}
          {vendor.address && <p><strong>Address:</strong> {vendor.address}</p>}
          {vendor.gstTanCin && <p><strong>GST/TAN/CIN:</strong> {vendor.gstTanCin}</p>}
          {vendor.bankAcc && <p><strong>Bank Account:</strong> {vendor.bankAcc}</p>}
          {vendor.ifsc && <p><strong>IFSC:</strong> {vendor.ifsc}</p>}
        </div>
      </div>

      {/* Services Grid */}
      {services.length === 0 ? (
        <div className="no-services">
          <h3>No Services Available</h3>
          <p>This vendor has no services assigned yet.</p>
        </div>
      ) : (
        <div className="services-grid">
          {services.map((service) => (
            <div key={service.id} className="service-card">
              <div className="service-header">
                <h3>{service.serviceTypeName || 'Unknown Service'}</h3>
                <button
                  className="btn-create-invoice"
                  onClick={() => handleCreateInvoice(service.id, service.serviceTypeName)}
                  title={`Create invoice for ${service.serviceTypeName}`}
                >
                  📄 Create Invoice
                </button>
              </div>
              <div className="service-details">
                <p><strong>TDS Rate:</strong> {service.tdsRate}%</p>
                <p><strong>Service ID:</strong> {service.id}</p>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default VendorServices;