import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true
});

// Error handler
const handleError = (error) => {
  if (error.response) {
    throw new Error(error.response.data.message || 'Server error');
  }
  throw new Error('Network error');
};

export const vendorAPI = {
  // Get all vendors
  getAllVendors: async () => {
    try {
      const response = await api.get('/vendors');
      return response.data;
    } catch (error) {
      handleError(error);
    }
  },

  // Get vendor by ID
  getVendorById: async (vendorId) => {
    try {
      const response = await api.get(`/vendors/${vendorId}`);
      return response.data;
    } catch (error) {
      handleError(error);
    }
  },

  // Create new vendor
  createVendor: async (vendorData) => {
    try {
      const response = await api.post('/vendors', vendorData);
      return response.data;
    } catch (error) {
      handleError(error);
    }
  },
    deleteVendor: async (vendorId) => {
      try {
        const response = await api.delete(`/vendors/${vendorId}`);
        return response.data;
      } catch (error) {
        handleError(error);
        throw error; // Important to throw again so UI knows deletion failed
      }
    },

  // Get vendor services
  getVendorServices: async (vendorId) => {
    try {
      const response = await api.get(`/vendor-services/vendor/${vendorId}`);
      return response.data;
    } catch (error) {
      handleError(error);
    }
  }
};

export const healthCheck = {
  checkBackend: async () => {
    try {
      const response = await api.get('/actuator/health');
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

// Improved fetchServiceTypes function with better error handling and response format handling
export const fetchServiceTypes = async () => {
  try {
    console.log('📡 Fetching service types from API...');
    const response = await api.get('/service-types');

    console.log('📦 Raw API response:', response);
    console.log('📋 Response data:', response.data);

    // Handle different possible response formats
    let serviceTypes;

    if (Array.isArray(response.data)) {
      // Direct array response
      serviceTypes = response.data;
    } else if (response.data && Array.isArray(response.data.data)) {
      // Wrapped in data property
      serviceTypes = response.data.data;
    } else if (response.data && Array.isArray(response.data.serviceTypes)) {
      // Wrapped in serviceTypes property
      serviceTypes = response.data.serviceTypes;
    } else if (response.data && typeof response.data === 'object') {
      // If it's an object, try to extract array values
      const values = Object.values(response.data);
      const arrayValue = values.find(value => Array.isArray(value));
      serviceTypes = arrayValue || [];
    } else {
      console.warn('⚠️ Unexpected response format:', response.data);
      serviceTypes = [];
    }

    console.log('✅ Processed service types:', serviceTypes);

    // Validate the structure of service types
    if (Array.isArray(serviceTypes)) {
      serviceTypes.forEach((type, index) => {
        if (!type.id || !type.name) {
          console.warn(`⚠️ Service type at index ${index} missing id or name:`, type);
        }
      });
    }

    return serviceTypes;

  } catch (error) {
    console.error('❌ Error fetching service types:', error);

    // Log more details about the error
    if (error.response) {
      console.error('📋 Error response data:', error.response.data);
      console.error('📊 Error response status:', error.response.status);
    }

    handleError(error);
  }
};