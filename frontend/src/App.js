// Replace your src/App.js with this content

import React, { useState } from 'react';
import './App.css';
import VendorForm from './components/VendorForm/VendorForm.js';
import VendorList from './components/VendorList/VendorList.js';
import NotificationBar from './components/NotificationBar/NotificationBar.js';

function App() {
  const [refreshTrigger, setRefreshTrigger] = useState(0);
  const [notifications, setNotifications] = useState([]);

  const handleVendorAdded = (vendor) => {
    setRefreshTrigger(prev => prev + 1);
    showNotification(
      'Success',
      `Vendor "${vendor.name}" has been added successfully!`,
      'success'
    );
  };

  const showNotification = (title, message, type = 'info') => {
    const id = Date.now();
    const newNotification = { id, title, message, type };

    setNotifications(prev => [...prev, newNotification]);
  };

  const removeNotification = (id) => {
    setNotifications(prev => prev.filter(notif => notif.id !== id));
  };

  return (
    <div className="App">
      {/* Notifications */}
      {notifications.map((notification, index) => (
        <div
          key={notification.id}
          style={{
            position: 'fixed',
            top: `${20 + (index * 80)}px`,
            right: '20px',
            zIndex: 1000 + index
          }}
        >
          <NotificationBar
            title={notification.title}
            message={notification.message}
            type={notification.type}
            onClose={() => removeNotification(notification.id)}
            autoClose={true}
            duration={5000}
          />
        </div>
      ))}

      {/* Header */}
      <header className="app-header">
        <div className="header-content">
          <div className="header-text">
            <h1>Vendor Management System</h1>
            <p>Streamline your vendor operations with ease</p>
          </div>
          <div className="header-stats">
            <div className="stat-card">
              <div className="stat-number">🏢</div>
              <div className="stat-label">Vendors</div>
            </div>
            <div className="stat-card">
              <div className="stat-number">📄</div>
              <div className="stat-label">Invoices</div>
            </div>
            <div className="stat-card">
              <div className="stat-number">💳</div>
              <div className="stat-label">Payments</div>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="main-content">
        <div className="content-container">
          <div className="content-grid">
            {/* Vendor Form Section */}
            <section className="form-section">
              <VendorForm onVendorAdded={handleVendorAdded} />
            </section>

            {/* Vendor List Section */}
            <section className="list-section">
              <VendorList
                refreshTrigger={refreshTrigger}
                onNotification={showNotification}
              />
            </section>
          </div>
        </div>
      </main>

      {/* Footer */}
      <footer className="app-footer">
        <div className="footer-content">
          <p>&copy; 2024 Vendor Management System. Built with React & Spring Boot.</p>
          <div className="footer-links">
            <span>Version 1.0.0</span>
            <span>•</span>
            <span>Status: ✅ Online</span>
          </div>
        </div>
      </footer>
    </div>
  );
}

export default App;