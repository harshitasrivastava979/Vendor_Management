import React, { useState } from 'react';
import { Routes, Route } from 'react-router-dom';
import Login from '../components/Login/Login';
import ProtectedRoute from '../components/ProtectedRoute/ProtectedRoute';
import VendorList from '../components/VendorList/VendorList';
import VendorForm from '../components/VendorForm/VendorForm';
import VendorServices from '../components/VendorServices/VendorServices';
import InvoiceForm from '../components/InvoiceForm/InvoiceForm';
import InvoiceList from '../components/InvoiceList/InvoiceList';

const Home = ({ showNotification }) => {
  const [refreshTrigger, setRefreshTrigger] = useState(0);
  const [selectedVendorId, setSelectedVendorId] = useState(null);
  const [editingVendor, setEditingVendor] = useState(null);

  const handleVendorAdded = (vendor) => {
    setRefreshTrigger(prev => prev + 1);
    setSelectedVendorId(vendor.id);
    setEditingVendor(null);
    if (showNotification) {
      showNotification(
        'Success',
        `Vendor "${vendor.name}" has been added successfully!`,
        'success'
      );
    }
  };

  const handleVendorUpdated = (vendor) => {
    setRefreshTrigger(prev => prev + 1);
    setEditingVendor(null);
    if (showNotification) {
      showNotification(
        'Success',
        `Vendor "${vendor.name}" has been updated successfully!`,
        'success'
      );
    }
  };

  return (
    <div className="home-container">
      {/* Top Section: Form and Vendor List */}
      <div className="top-section">
        <section className="form-section">
          <VendorForm
            onVendorAdded={handleVendorAdded}
            editingVendor={editingVendor}
            onVendorUpdated={handleVendorUpdated}
          />
        </section>

        <section className="list-section">
          <VendorList
            refreshTrigger={refreshTrigger}
            onNotification={showNotification}
            onVendorSelected={(vendorId) => setSelectedVendorId(vendorId)}
            onEditVendor={setEditingVendor}
          />
        </section>
      </div>

      {/* Bottom Section: Vendor Services (when vendor is selected) */}
      {selectedVendorId && (
        <section className="vendor-service-section">
          <div className="service-section-header">
            <h2>Selected Vendor Services</h2>
            <button 
              className="clear-selection-btn"
              onClick={() => setSelectedVendorId(null)}
            >
              Clear Selection
            </button>
          </div>
          <VendorServices vendorId={selectedVendorId} />
        </section>
      )}
    </div>
  );
};

const AppRoutes = ({ showNotification }) => {
  return (
    <Routes>
      {/* Public routes */}
      <Route path="/login" element={<Login />} />
      
      {/* Protected routes */}
      <Route path="/" element={
        <ProtectedRoute>
          <Home showNotification={showNotification} />
        </ProtectedRoute>
      } />
      <Route path="/vendors/new" element={
        <ProtectedRoute>
          <VendorForm />
        </ProtectedRoute>
      } />
      <Route path="/vendors/edit/:vendorId" element={
        <ProtectedRoute>
          <VendorForm />
        </ProtectedRoute>
      } />
      <Route path="/vendor/:vendorId/services" element={
        <ProtectedRoute>
          <VendorServices />
        </ProtectedRoute>
      } />
      <Route path="/create-invoice/:vendorServiceId" element={
        <ProtectedRoute>
          <InvoiceForm />
        </ProtectedRoute>
      } />
      <Route path="/invoices/:vendorServiceId" element={
        <ProtectedRoute>
          <InvoiceList />
        </ProtectedRoute>
      } />
    </Routes>
  );
};

export default AppRoutes;