// Replace your src/App.js with this content

import React, { useState } from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import AppRoutes from './routes/AppRoutes.js';
import NotificationBar from './components/NotificationBar/NotificationBar.js';
import './App.css';

function App() {
  const [notifications, setNotifications] = useState([]);

  const showNotification = (title, message, type = 'info') => {
    const id = Date.now();
    const newNotification = { id, title, message, type };
    setNotifications(prev => [...prev, newNotification]);
  };

  const removeNotification = (id) => {
    setNotifications(prev => prev.filter(notif => notif.id !== id));
  };

  return (
    <Router>
      <div className="app">
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
            <AppRoutes showNotification={showNotification} />
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
    </Router>
  );
}

export default App;