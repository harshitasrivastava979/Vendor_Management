import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add response interceptor for better error handling
api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('API Error:', error.response?.data || error.message);
    return Promise.reject(error);
  }
);

export const vendorAPI = {
  // Get all vendors
  getAllVendors: () => api.get('/vendors'),

  // Create new vendor
  createVendor: (vendorData) => api.post('/vendors', vendorData),

  // Test API connection
  testConnection: () => api.get('/vendors/test'),
};

export default api;