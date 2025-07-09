import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import './Login.css';

const Login = () => {
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    confirmPassword: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const { login, register } = useAuth();
  const navigate = useNavigate();

  const handleInputChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
    setError(''); // Clear error when user starts typing
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');

    try {
      if (isLogin) {
        // Login
        await login(formData.username, formData.password);
        setSuccess('Login successful! Redirecting...');
        setTimeout(() => {
          navigate('/');
        }, 1000);
      } else {
        // Register
        if (formData.password !== formData.confirmPassword) {
          throw new Error('Passwords do not match');
        }
        await register(formData.username, formData.password);
        setSuccess('Registration successful! Please login.');
        setIsLogin(true);
        setFormData({ username: '', password: '', confirmPassword: '' });
      }
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  const toggleMode = () => {
    setIsLogin(!isLogin);
    setFormData({ username: '', password: '', confirmPassword: '' });
    setError('');
    setSuccess('');
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <div className="login-header">
          <h1>Vendor Management System</h1>
          <p>{isLogin ? 'Welcome back! Please login to your account.' : 'Create a new account to get started.'}</p>
        </div>

        {error && (
          <div className="error-message">
            <span>⚠️ {error}</span>
          </div>
        )}

        {success && (
          <div className="success-message">
            <span>✅ {success}</span>
          </div>
        )}

        <form onSubmit={handleSubmit} className="login-form">
          <div className="form-group">
            <label htmlFor="username">Username</label>
            <input
              type="text"
              id="username"
              name="username"
              value={formData.username}
              onChange={handleInputChange}
              placeholder="Enter your username"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleInputChange}
              placeholder="Enter your password"
              required
            />
          </div>

          {!isLogin && (
            <div className="form-group">
              <label htmlFor="confirmPassword">Confirm Password</label>
              <input
                type="password"
                id="confirmPassword"
                name="confirmPassword"
                value={formData.confirmPassword}
                onChange={handleInputChange}
                placeholder="Confirm your password"
                required
              />
            </div>
          )}

          <button 
            type="submit" 
            className="submit-btn" 
            disabled={loading}
          >
            {loading ? (
              <span>🔄 {isLogin ? 'Logging in...' : 'Creating account...'}</span>
            ) : (
              <span>{isLogin ? 'Login' : 'Register'}</span>
            )}
          </button>
        </form>

        <div className="login-footer">
          <p>
            {isLogin ? "Don't have an account? " : "Already have an account? "}
            <button 
              type="button" 
              className="toggle-btn" 
              onClick={toggleMode}
            >
              {isLogin ? 'Register here' : 'Login here'}
            </button>
          </p>
        </div>

        <div className="login-features">
          <div className="feature">
            <span>🔐</span>
            <span>Secure Authentication</span>
          </div>
          <div className="feature">
            <span>📊</span>
            <span>Vendor Management</span>
          </div>
          <div className="feature">
            <span>📄</span>
            <span>Invoice Tracking</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login; 