import React, { useState, useEffect } from 'react';
import { vendorAPI } from '../../services/api.js';
import { useNavigate } from 'react-router-dom';
import './VendorServices.css';

const VendorServices = ({ vendorId }) => {
  const [vendor, setVendor] = useState(null);
  const [services, setServices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

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
        console.log('Vendor data:', vendorData);
        console.log('Vendor services data:', servicesData);
        if (servicesData.length > 0) {
          console.log('First service structure:', servicesData[0]);
        }
      } catch (err) {
        setError(err.message || 'Failed to fetch vendor services');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [vendorId]);

  const handleCreateInvoice = (vendorServiceId) => {
    if (!vendorServiceId) {
      console.error('Cannot create invoice: vendorServiceId is null or undefined');
      return;
    }
    navigate(`/create-invoice/${vendorServiceId}`);
  };

  const handleViewInvoices = (vendorServiceId) => {
    if (!vendorServiceId) {
      console.error('Cannot view invoices: vendorServiceId is null or undefined');
      return;
    }
    navigate(`/invoices/${vendorServiceId}`);
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

          {vendor.address && <p><strong>Address:</strong> {vendor.address}</p>}
          {vendor.bankName && <p><strong>BANK:</strong> {vendor.bankName}</p>}
          {vendor.gst && <p><strong>GST:</strong> {vendor.gst}</p>}


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
          {services.map((service, index) => (
           <div key={service.id || `service-${index}`} className="service-card">
              <div className="service-header">
                <h3>{service.serviceType?.name || 'Unknown Service'}</h3>
                <div className="service-actions">
                  <button
                    className="btn-view-invoices"
                    onClick={() => handleViewInvoices(service.id)}
                    title={`View invoices for ${service.serviceType?.name}`}
                    disabled={!service.id}
                  >
                    📋 View Invoices
                  </button>
                  <button
                    className="btn-create-invoice"
                    onClick={() => handleCreateInvoice(service.id)}
                    title={`Create invoice for ${service.serviceType?.name}`}
                    disabled={!service.id}
                  >
                    📄 Create Invoice
                  </button>
                </div>
              </div>
              <div className="service-details">
                <p><strong>TDS Rate:</strong> {service.tdsRate}%</p>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default VendorServices;