import { protectedAPI } from './authService';

// Error handler
const handleError = (error) => {
  if (error.response) {
    throw new Error(error.response.data.message || 'Server error');
  }
  throw new Error('Network error');
};

export const vendorAPI = {

  // Vendor CRUD and service-related APIs...
  createServiceType: async (serviceTypeData) => {
    try {
      const response = await protectedAPI.post('/service-types', serviceTypeData);
      return response.data;
    } catch (error) {
      handleError(error);
      throw error;
    }
  },

  getAllVendors: async () => {
    try {
      const response = await protectedAPI.get('/vendors');
      return response.data;
    } catch (error) {
      handleError(error);
    }
  },
  deleteInvoice: async (invoiceId) => {
    try {
      const response = await protectedAPI.delete(`/invoices/${invoiceId}`);
      return response.data;
    } catch (error) {
      handleError(error);
      throw error;
    }
  },


  getVendorById: async (vendorId) => {
    try {
      const response = await protectedAPI.get(`/vendors/${vendorId}`);
      return response.data;
    } catch (error) {
      handleError(error);
    }
  },

  createVendor: async (vendorData) => {
    try {
      const response = await protectedAPI.post('/vendors', vendorData);
      return response.data;
    } catch (error) {
      handleError(error);
    }
  },

  deleteVendor: async (vendorId) => {
    try {
      const response = await protectedAPI.delete(`/vendors/${vendorId}`);
      return response.data;
    } catch (error) {
      handleError(error);
      throw error;
    }
  },

  updateVendor: async (vendorId, vendorData) => {
    try {
      const response = await protectedAPI.put(`/vendors/${vendorId}`, vendorData);
      return response.data;
    } catch (error) {
      handleError(error);
    }
  },

  getVendorServices: async (vendorId) => {
    try {
      const response = await protectedAPI.get(`/vendor-services/vendor/${vendorId}`);
      return response.data;
    } catch (error) {
      handleError(error);
    }
  },

  getVendorServiceById: async (vendorServiceId) => {
    try {
      const response = await protectedAPI.get(`/vendor-services/${vendorServiceId}`);
      return response.data;
    } catch (error) {
      handleError(error);
    }
  },

  createInvoice: async (formData, isMultipart = false) => {
    try {
      const config = isMultipart
        ? { headers: { 'Content-Type': 'multipart/form-data' } }
        : {};
      const response = await protectedAPI.post('/invoices', formData, config);
      return response.data;
    } catch (error) {
      handleError(error);
    }
  },

  getInvoicesByVendorService: async (vendorServiceId) => {
    try {
      const response = await protectedAPI.get(`/invoices/by-vendor-service/${vendorServiceId}`);
      return response.data;
    } catch (error) {
      handleError(error);
    }
  },

  getInvoicesByVendorServiceWithDateRange: async (vendorServiceId, startDate = null, endDate = null) => {
    try {
      let url = `/invoices/by-vendor-service/${vendorServiceId}`;
      const params = new URLSearchParams();
      if (startDate) params.append('from', startDate);
      if (endDate) params.append('to', endDate);
      if (params.toString()) url += `?${params.toString()}`;
      const response = await protectedAPI.get(url);
      return response.data;
    } catch (error) {
      handleError(error);
    }
  },

  getInvoicesByVendorServiceWithCreatedAtDateRange: async (vendorServiceId, startDate = null, endDate = null) => {
    if (!startDate || !endDate) return [];
    try {
      let url = `/invoices/by-vendor-service/${vendorServiceId}/created-at`;
      const params = new URLSearchParams();
      const startDateTime = new Date(startDate); startDateTime.setUTCHours(0, 0, 0, 0);
      const endDateTime = new Date(endDate); endDateTime.setUTCHours(23, 59, 59, 999);
      params.append('from', startDateTime.toISOString());
      params.append('to', endDateTime.toISOString());
      url += `?${params.toString()}`;
      const response = await protectedAPI.get(url);
      return response.data;
    } catch (error) {
      handleError(error);
    }
  },

  // ✅ Fixed Excel export
  exportInvoicesToExcel: async (vendorServiceId, startDate = null, endDate = null) => {
    try {
      const params = new URLSearchParams();
      params.append('vendorServiceId', vendorServiceId);
      if (startDate) params.append('startDate', startDate);
      if (endDate) params.append('endDate', endDate);
      const response = await protectedAPI.get(`/excel/invoices/download?${params.toString()}`, {
        responseType: 'blob',
        headers: { 'Accept': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' }
      });
      return response.data;
    } catch (error) {
      console.error('Excel Export Error:', error);
      handleError(error);
    }
  },

  // ✅ Fixed CSV export
  exportInvoicesToCsv: async (vendorServiceId, startDate = null, endDate = null) => {
    try {
      const params = new URLSearchParams();
      params.append('vendorServiceId', vendorServiceId);
      if (startDate) params.append('startDate', startDate);
      if (endDate) params.append('endDate', endDate);
      const response = await protectedAPI.get(`/excel/invoices/download-csv?${params.toString()}`, {
        responseType: 'blob',
        headers: { 'Accept': 'text/csv' }
      });
      return response.data;
    } catch (error) {
      console.error('CSV Export Error:', error);
      handleError(error);
    }
  },

  // ✅ Fixed Created At Excel export
  exportCreatedInvoicesToExcel: async (vendorServiceId, createdFrom = null, createdTo = null) => {
    try {
      if (!createdFrom || !createdTo) throw new Error('Both start and end dates are required for Created At export');
      const params = new URLSearchParams();
      params.append('vendorServiceId', vendorServiceId);
      const from = new Date(createdFrom); from.setHours(0, 0, 0, 0);
      const to = new Date(createdTo); to.setHours(23, 59, 59, 999);
      params.append('from', from.toISOString());
      params.append('to', to.toISOString());
      const response = await protectedAPI.get(`/excel/invoices/download/created-at?${params.toString()}`, {
        responseType: 'blob',
        headers: { 'Accept': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' }
      });
      return response.data;
    } catch (error) {
      console.error('Created At Excel Export Error:', error);
      handleError(error);
    }
  },

  // ✅ Fixed Created At CSV export
  exportCreatedInvoicesToCsv: async (vendorServiceId, createdFrom = null, createdTo = null) => {
    try {
      if (!createdFrom || !createdTo) throw new Error('Both start and end dates are required for Created At export');
      const params = new URLSearchParams();
      params.append('vendorServiceId', vendorServiceId);
      const from = new Date(createdFrom); from.setHours(0, 0, 0, 0);
      const to = new Date(createdTo); to.setHours(23, 59, 59, 999);
      params.append('from', from.toISOString());
      params.append('to', to.toISOString());
      const response = await protectedAPI.get(`/excel/invoices/download-csv/created-at?${params.toString()}`, {
        responseType: 'blob',
        headers: { 'Accept': 'text/csv' }
      });
      return response.data;
    } catch (error) {
      console.error('Created At CSV Export Error:', error);
      handleError(error);
    }
  },

  previewInvoiceBill: async (invoiceId) => {
    return protectedAPI.get(`/invoices/${invoiceId}/preview`);
  },

  downloadInvoiceBill: async (invoiceId) => {
    return protectedAPI.get(`/invoices/${invoiceId}/download`, { responseType: 'blob' });
  }
};

// Health check
export const healthCheck = {
  checkBackend: async () => {
    try {
      const response = await protectedAPI.get('/actuator/health');
      return { status: 'online', data: response.data };
    } catch (error) {
      return { status: 'offline', error: error.message };
    }
  }
};

// Fetch service types
export const fetchServiceTypes = async () => {
  try {
    const response = await protectedAPI.get('/service-types');
    const data = response.data;
    if (Array.isArray(data)) return data;
    if (Array.isArray(data?.data)) return data.data;
    if (Array.isArray(data?.serviceTypes)) return data.serviceTypes;
    const values = Object.values(data || {});
    return values.find(v => Array.isArray(v)) || [];
  } catch (error) {
    console.error('❌ Error fetching service types:', error);
    handleError(error);
  }
};
