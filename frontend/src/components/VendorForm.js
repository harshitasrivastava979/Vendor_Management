import React, { useState } from 'react';
import { createVendor } from '../services/api';

const VendorForm = ({ onVendorAdded }) => {
  const [formData, setFormData] = useState({ name: '', email: '', tdsRate: '' });

  const handleChange = e => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = e => {
    e.preventDefault();
    createVendor(formData)
      .then(() => {
        onVendorAdded();  // refresh the vendor list
        setFormData({ name: '', email: '', tdsRate: '' });
      })
      .catch(err => console.error("Error creating vendor:", err));
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Add New Vendor</h2>
      <input name="name" value={formData.name} onChange={handleChange} placeholder="Vendor Name" required />
      <input name="email" value={formData.email} onChange={handleChange} placeholder="Email" required />
      <input name="tdsRate" value={formData.tdsRate} onChange={handleChange} placeholder="TDS Rate" required />
      <button type="submit">Add Vendor</button>
    </form>
  );
};

export default VendorForm;
