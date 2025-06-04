// Create this file: src/services/api.js

import axios from 'axios';

// API Configuration
const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

// Create axios instance with default config
const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor for logging
api.interceptors.request.use(
  (config) => {
    console.log(`🚀 API Request: ${config.method?.toUpperCase()} ${config.url}`, config.data);
    return config;
  },
  (error) => {
    console.error('❌ API Request Error:', error);
    return Promise.reject(error);
  }
);

// Response interceptor for error handling
api.interceptors.response.use(
  (response) => {
    console.log(`✅ API Response: ${response.status}`, response.data);
    return response;
  },
  (error) => {
    console.error('❌ API Response Error:', error.response?.data || error.message);

    // Handle different error scenarios
    if (error.code === 'ECONNREFUSED' || error.code === 'ERR_NETWORK') {
      throw new Error('Backend server is not running. Please start your Spring Boot application on port 8080.');
    }

    if (error.response?.status === 404) {
      throw new Error('API endpoint not found. Please check your Spring Boot controller.');
    }

    if (error.response?.status >= 500) {
      throw new Error('Server error. Please check your Spring Boot application logs.');
    }

    throw error;
  }
);

// Vendor API endpoints
export const vendorAPI = {
  // Get all vendors
  getAllVendors: async () => {
    try {
      const response = await api.get('/vendors');
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch vendors: ${error.message}`);
    }
  },

  // Get vendor by ID
  getVendorById: async (id) => {
    try {
      const response = await api.get(`/vendors/${id}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch vendor: ${error.message}`);
    }
  },

  // Create new vendor
  createVendor: async (vendorData) => {
    try {
      const response = await api.post('/vendors', vendorData);
      return response.data;
    } catch (error) {
      if (error.response?.status === 400) {
        throw new Error('Invalid vendor data. Please check your input.');
      }
      throw new Error(`Failed to create vendor: ${error.message}`);
    }
  },

  // Update vendor
  updateVendor: async (id, vendorData) => {
    try {
      const response = await api.put(`/vendors/${id}`, vendorData);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to update vendor: ${error.message}`);
    }
  },

  // Delete vendor
  deleteVendor: async (id) => {
    try {
      await api.delete(`/vendors/${id}`);
      return true;
    } catch (error) {
      throw new Error(`Failed to delete vendor: ${error.message}`);
    }
  },

  // Search vendors
  searchVendors: async (searchTerm) => {
    try {
      const response = await api.get(`/vendors/search?q=${encodeURIComponent(searchTerm)}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to search vendors: ${error.message}`);
    }
  },

  // Test API connection
  testConnection: async () => {
    try {
      const response = await api.get('/vendors/test');
      return response.data;
    } catch (error) {
      throw new Error(`Connection test failed: ${error.message}`);
    }
  }
};

// Invoice API endpoints (for future use)
export const invoiceAPI = {
  // Get invoices for a vendor
  getVendorInvoices: async (vendorId) => {
    try {
      const response = await api.get(`/vendors/${vendorId}/invoices`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch invoices: ${error.message}`);
    }
  },

  // Create invoice
  createInvoice: async (invoiceData) => {
    try {
      const response = await api.post('/invoices', invoiceData);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to create invoice: ${error.message}`);
    }
  }
};

// Health check endpoint
export const healthAPI = {
  checkBackendStatus: async () => {
    try {
      const response = await api.get('/health');
      return {
        status: 'online',
        data: response.data
      };
    } catch (error) {
      return {
        status: 'offline',
        error: error.message
      };
    }
  }
};

export default api;