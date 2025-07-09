import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

// Create axios instance for auth endpoints
const authAPI = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Create axios instance for protected endpoints
const protectedAPI = axios.create({
  baseURL: `${API_BASE_URL}/api`,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to protected requests
protectedAPI.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Handle token expiration
protectedAPI.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('authToken');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export const authService = {
  // Register new user
  register: async (username, password) => {
    try {
      const response = await authAPI.post('/auth/register', {
        username,
        password
      });
      return response.data;
    } catch (error) {
      throw new Error(error.response?.data || 'Registration failed');
    }
  },

  // Login user
  login: async (username, password) => {
    try {
      const response = await authAPI.post('/auth/login', {
        username,
        password
      });

      const { token } = response.data;

      // Store token and user info
      localStorage.setItem('authToken', token);
      localStorage.setItem('user', JSON.stringify({ username }));

      return response.data;
    } catch (error) {
      throw new Error(error.response?.data || 'Login failed');
    }
  },

  // Logout user
  logout: () => {
    localStorage.removeItem('authToken');
    localStorage.removeItem('user');
  },

  // Check if user is authenticated
  isAuthenticated: () => {
    return !!localStorage.getItem('authToken');
  },

  // Get current user
  getCurrentUser: () => {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  },

  // Get auth token
  getToken: () => {
    return localStorage.getItem('authToken');
  }
};

// Export the protected API for use in other services
export { protectedAPI };
