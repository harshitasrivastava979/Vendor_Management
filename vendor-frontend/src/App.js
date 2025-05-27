// Add this to your App.js to test the API connection

import React, { useState } from 'react';
import './App.css';
import VendorForm from './components/VendorForm';
import VendorList from './components/VendorList';

function App() {
  const [refreshTrigger, setRefreshTrigger] = useState(0);
  const [apiTestResult, setApiTestResult] = useState('');

  const handleVendorAdded = () => {
    setRefreshTrigger(prev => prev + 1);
  };

  // API Test Function
  const testAPI = async () => {
    setApiTestResult('Testing...');

    try {
      console.log('🧪 Testing API connection...');

      // Test 1: Health check
      const testResponse = await fetch('http://localhost:8080/api/vendors/test');
      if (!testResponse.ok) {
        throw new Error(`Test endpoint failed: ${testResponse.status}`);
      }
      const testData = await testResponse.text();
      console.log('✅ Test endpoint:', testData);

      // Test 2: Get vendors
      const vendorsResponse = await fetch('http://localhost:8080/api/vendors');
      if (!vendorsResponse.ok) {
        throw new Error(`Vendors endpoint failed: ${vendorsResponse.status}`);
      }
      const vendorsData = await vendorsResponse.json();
      console.log('✅ Vendors data:', vendorsData);

      // Test 3: Test with axios (your actual API service)
      const { vendorAPI } = await import('./services/api');
      const axiosTest = await vendorAPI.testConnection();
      const axiosVendors = await vendorAPI.getAllVendors();

      console.log('✅ Axios test:', axiosTest.data);
      console.log('✅ Axios vendors:', axiosVendors.data);

      setApiTestResult(`✅ SUCCESS!
      Test: ${testData}
      Vendors found: ${vendorsData.length}
      Axios working: ${axiosTest.data}`);

    } catch (error) {
      console.error('❌ API Test Failed:', error);
      setApiTestResult(`❌ FAILED: ${error.message}`);
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>Vendor Management System</h1>
        <p>Manage your vendors efficiently</p>

        {/* API Test Section */}
        <div style={{
          margin: '20px 0',
          padding: '15px',
          backgroundColor: 'rgba(255,255,255,0.1)',
          borderRadius: '8px'
        }}>
          <button
            onClick={testAPI}
            style={{
              padding: '10px 20px',
              backgroundColor: '#28a745',
              color: 'white',
              border: 'none',
              borderRadius: '5px',
              cursor: 'pointer',
              marginRight: '10px'
            }}
          >
            🧪 Test API Connection
          </button>

          {apiTestResult && (
            <div style={{
              marginTop: '10px',
              padding: '10px',
              backgroundColor: 'rgba(255,255,255,0.9)',
              color: '#333',
              borderRadius: '5px',
              whiteSpace: 'pre-line',
              textAlign: 'left'
            }}>
              {apiTestResult}
            </div>
          )}
        </div>
      </header>

      <main className="main-content">
        <div className="content-grid">
          <div className="form-section">
            <VendorForm onVendorAdded={handleVendorAdded} />
          </div>

          <div className="list-section">
            <VendorList refreshTrigger={refreshTrigger} />
          </div>
        </div>
      </main>
    </div>
  );
}

export default App;