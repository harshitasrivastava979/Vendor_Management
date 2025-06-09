import React, { useState } from 'react';
import { Routes, Route } from 'react-router-dom';
import VendorList from '../components/VendorList/VendorList.js';
import VendorForm from '../components/VendorForm/VendorForm.js';
import VendorServices from '../components/VendorServices/VendorServices.js';
import InvoiceForm from '../components/InvoiceForm/InvoiceForm.js';

const Home = ({ showNotification }) => {
  const [refreshTrigger, setRefreshTrigger] = useState(0);
  const [selectedVendorId, setSelectedVendorId] = useState(null);

  const handleVendorAdded = (vendor) => {
    // Trigger a refresh of the vendor list
    setRefreshTrigger(prev => prev + 1);
    // Optionally store the vendor ID for which you want to fetch vendor service details
    setSelectedVendorId(vendor.id);

    if (showNotification) {
      showNotification(
        'Success',
        `Vendor "${vendor.name}" has been added successfully!`,
        'success'
      );
    }
  };

  return (
    <div className="content-grid">
      <section className="form-section">
        <VendorForm onVendorAdded={handleVendorAdded} />
      </section>

      <section className="list-section">
        <VendorList
          refreshTrigger={refreshTrigger}
          onNotification={showNotification}
          onVendorSelected={(vendorId) => setSelectedVendorId(vendorId)}
        />
      </section>

      {/* Optionally, you can show vendor service details directly on the home page */}
      {selectedVendorId && (
        <section className="vendor-service-section">
          <VendorServices vendorId={selectedVendorId} />
        </section>
      )}
    </div>
  );
};



const AppRoutes = ({ showNotification }) => {
  return (
    <Routes>
      <Route path="/" element={<Home showNotification={showNotification} />} />
      <Route path="/vendors/new" element={<VendorForm />} />
      <Route path="/vendors/edit/:vendorId" element={<VendorForm />} />
      <Route path="/vendor/:vendorId/services" element={<VendorServices />} />
      <Route path="/create-invoice/:vendorServiceId" element={<InvoiceForm />} />
    </Routes>
  );
};

export default AppRoutes;