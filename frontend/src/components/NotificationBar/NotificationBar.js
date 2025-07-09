// Create this file: src/components/NotificationBar/NotificationBar.js

import React, { useEffect } from 'react';
import './NotificationBar.css';

const NotificationBar = ({
  title,
  message,
  type = 'info',
  onClose,
  autoClose = true,
  duration = 5000
}) => {
  useEffect(() => {
    if (autoClose && onClose) {
      const timer = setTimeout(() => {
        onClose();
      }, duration);

      return () => clearTimeout(timer);
    }
  }, [autoClose, duration, onClose]);

  const getIcon = () => {
    switch (type) {
      case 'success':
        return '✅';
      case 'error':
        return '❌';
      case 'warning':
        return '⚠️';
      case 'info':
        return 'ℹ️';
      default:
        return 'ℹ️';
    }
  };

  const handleClose = () => {
    if (onClose) {
      onClose();
    }
  };

  return (
    <div className={`notification-bar notification-${type}`}>
      <div className="notification-content">
        <div className="notification-icon">
          {getIcon()}
        </div>
        <div className="notification-text">
          <div className="notification-title">{title}</div>
          {message && (
            <div className="notification-message">{message}</div>
          )}
        </div>
        <button
          className="notification-close"
          onClick={handleClose}
          aria-label="Close notification"
        >
          ×
        </button>
      </div>

      {autoClose && (
        <div className="notification-progress">
          <div
            className="notification-progress-bar"
            style={{ animationDuration: `${duration}ms` }}
          ></div>
        </div>
      )}
    </div>
  );
};

export default NotificationBar;