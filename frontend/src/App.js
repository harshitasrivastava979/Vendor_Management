// Replace your src/App.js with this content

import React, { useState } from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import AppRoutes from './routes/AppRoutes';
import Header from './components/Header/Header';
import NotificationBar from './components/NotificationBar/NotificationBar';
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
    <AuthProvider>
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
          <Header />

          {/* Main Content */}
          <main className="main-content">
            <div className="content-container">
              <AppRoutes showNotification={showNotification} />
            </div>
          </main>

          {/* Footer */}
          <footer className="app-footer">
            <div className="footer-content">
              <p>Vendor Management System</p>
              <div className="footer-links">
                <span></span>
                <span></span>
                <span>Status: ✅ Online</span>
              </div>
            </div>
          </footer>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;