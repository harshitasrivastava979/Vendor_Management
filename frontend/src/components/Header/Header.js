import React from 'react';
import { useAuth } from '../../context/AuthContext';
import './Header.css';

const Header = () => {
  const { user, logout } = useAuth();

  const handleLogout = () => {
    logout();
    // The AuthContext will handle the redirect
  };

  return (
    <header className="app-header">
      <div className="header-content">
        <div className="header-text">
          <h1>Vendor Management System</h1>
          <p>Streamline your vendor operations with ease</p>
        </div>
        
        <div className="header-user-section">
          <div className="user-info">
            <span className="user-avatar">👤</span>
            <span className="username">Welcome, {user?.username || 'User'}!</span>
          </div>
          
          <button 
            className="logout-btn" 
            onClick={handleLogout}
            title="Logout"
          >
            🚪 Logout
          </button>
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
  );
};

export default Header; 