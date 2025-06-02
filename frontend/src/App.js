import React, { useState } from 'react';
import VendorList from './components/VendorList';
import VendorForm from './components/VendorForm';

function App() {
  const [refresh, setRefresh] = useState(false);

  const handleVendorAdded = () => setRefresh(prev => !prev);

  return (
    <div className="App">
      <h1>Vendor Management</h1>
      <VendorForm onVendorAdded={handleVendorAdded} />
      <VendorList key={refresh} />
    </div>
  );
}

export default App;
