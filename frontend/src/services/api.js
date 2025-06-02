// frontend/src/services/api.js

export const fetchVendors = async () => {
  const response = await fetch('http://localhost:8080/api/vendors');
  if (!response.ok) {
    throw new Error('Failed to fetch vendors');
  }
  return response.json();
};
export const createVendor = async (vendorData) => {
  const response = await fetch('http://localhost:8080/api/vendors', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(vendorData),
  });

  if (!response.ok) {
    throw new Error('Failed to create vendor');
  }
  return response.json();
};
