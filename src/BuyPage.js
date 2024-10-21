import React, { useState, useContext, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import NavBar from './Navbar';
import { usePayment } from './PaymentContext'; // Use the correct import
import { useAuth } from './AuthProvider'; // Assume AuthContext stores user info
import axios from 'axios';

const BuyPage = () => {
  const { stockSymbol } = useParams();
  const { balance, updateBalance } = usePayment(); // Use the payment context here
  const { user } = useAuth();
  const [stockDetails, setStockDetails] = useState(null);
  const [quantity, setQuantity] = useState(1);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  // Fetch stock details when the component mounts
  useEffect(() => {
    const fetchStockDetails = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/stocks/details/${stockSymbol}`);
        setStockDetails(response.data);
      } catch (error) {
        console.error('Error fetching stock details:', error);
        setError('Failed to fetch stock details. Please try again.');
      }
    };

    fetchStockDetails();
  }, [stockSymbol]);

  // Handle the purchase logic
  const handleBuy = async () => {
    if (!stockDetails) return;

    const totalCost = stockDetails.currentPrice * quantity;

    if (totalCost > balance) {
      setError('Insufficient funds to complete this purchase.');
      return;
    }

    try {
      const purchaseData = {
        stockSymbol: stockSymbol,
        quantity: quantity,
        price: stockDetails.currentPrice,
        userId: user.id // Ensure user.id is correctly fetched from AuthContext
      };

      const response = await axios.post('http://localhost:8080/api/portfolio/buy', purchaseData);
      console.log('Purchase successful:', response.data);

      // Update the user's balance
      updateBalance(-totalCost); // Update balance with the purchase cost

      // Redirect to portfolio page or show success message
      navigate('/portfolio');
    } catch (error) {
      console.error('Error during purchase:', error);
      setError('Failed to complete the purchase. Please try again.');
    }
  };

  return (
    <div className="container">
      <NavBar />
      <h1>Buy {stockSymbol}</h1>
      {stockDetails && (
        <>
          <p>Current Price: ${stockDetails.currentPrice.toFixed(2)}</p>
          <p>Available Balance: ${balance.toFixed(2)}</p>
          <input
            type="number"
            value={quantity}
            onChange={(e) => setQuantity(Number(e.target.value))}
            min="1"
          />
          <button onClick={handleBuy}>Confirm Purchase</button>
          {error && <p style={{ color: 'red' }}>{error}</p>}
        </>
      )}
      {!stockDetails && <p>Loading stock details...</p>}
    </div>
  );
};

export default BuyPage;