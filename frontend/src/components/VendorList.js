import React, { useEffect, useState } from 'react';
import { fetchVendors } from '../services/api';


const VendorList = () => {
  const [vendors, setVendors] = useState([]);

  useEffect(() => {
    fetchVendors()
      .then(res => setVendors(res.data))
      .catch(err => console.error("Error fetching vendors:", err));
  }, []);

  return (
    <div>
      <h2>Vendor List</h2>
      <ul>
        {vendors.map(vendor => (
          <li key={vendor.id}>{vendor.name} - {vendor.email}</li>
        ))}
      </ul>
    </div>
  );
};

export default VendorList;
