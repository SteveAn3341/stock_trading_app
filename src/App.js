import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './Homepage'; // Import HomePage component
import LoginPage from './LoginPage'; // Import LoginPage component
import Register from './Register';
import BuyPage from './BuyPage';
import UserPortfolio from './UserPortfolio';
// import HistoryPage from './Historypage';
import PaymentPage from './PaymentPage';
import { PaymentProvider } from './PaymentContext';
import { AuthProvider } from './AuthProvider';
import { UserProvider } from './UserContext';


function App() {
  return (
    <AuthProvider>
    <UserProvider>
    <PaymentProvider>
    <Router>
      <div>
         
        {/* Define Routes */}
        <Routes>
          {/* The Home Page will be the default route */}
          <Route path="/" element={<HomePage />} /> 
          <Route path="/login" element={<LoginPage />} /> 
          <Route path="/register" element={<Register />} /> 
          <Route path="/buy/:stockSymbol" element={<BuyPage />} />
          <Route path="/portfolio" element={<UserPortfolio />} /> 
          {/* <Route path="/history" element={<HistoryPage />} />  */}
          <Route path="/payment" element={<PaymentPage />} />
        </Routes>
      </div>
    </Router>
    </PaymentProvider>
    </UserProvider>
    </AuthProvider>
  );
}

export default App;