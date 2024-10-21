import React, { useState, useEffect, useContext, useCallback } from 'react';
import NavBar from './Navbar';
import axios from 'axios';
import { useAuth } from './AuthProvider';
import { PaymentContext } from './PaymentContext';

const UserPortfolio = () => {
  const [portfolio, setPortfolio] = useState(null);
  const [sellQuantities, setSellQuantities] = useState({});
  const [totalInvestment, setTotalInvestment] = useState(0);
  const { balance, updateBalance } = useContext(PaymentContext);
  const { user } = useAuth();
  const [loading, setLoading] = useState(true);

  const fetchUserPortfolio = useCallback(async () => {
    if (!user) return;
    setLoading(true);
    try {
      const response = await axios.get(`http://localhost:8080/api/portfolio/${user.id}`);
      console.log('Portfolio response:', response.data);

      setPortfolio(response.data);

      // Calculate total investment if purchases exist
      if (response.data.purchases && response.data.purchases.length > 0) {
        const totalInvestmentValue = response.data.purchases.reduce((total, purchase) => {
          if (purchase.price && purchase.quantity) {
            return total + (purchase.price * purchase.quantity);
          }
          return total;
        }, 0);
        console.log('Total Investment:', totalInvestmentValue);
        setTotalInvestment(totalInvestmentValue);
      } else {
        setTotalInvestment(0);
      }
    } catch (error) {
      console.error('Error fetching portfolio:', error.response ? error.response.data : error.message);
    } finally {
      setLoading(false);
    }
  }, [user]);

  useEffect(() => {
    if (user) {
      fetchUserPortfolio();
    }
  }, [user, fetchUserPortfolio]);

  const handleSellClick = async (symbol, purchaseId) => {
    if (!user) return;
    const quantityToSell = sellQuantities[purchaseId] || 1;

    if (quantityToSell <= 0) {
        console.error('Invalid quantity to sell:', quantityToSell);
        return;  // Exit early if quantity is invalid
    }

    try {
        console.log(`Attempting to sell ${quantityToSell} shares of ${symbol} for user: ${user.id}`);
        const response = await axios.post(`http://localhost:8080/api/portfolio/sell/${symbol}`, null, {
            params: { 
                userId: user.id, 
                quantity: quantityToSell 
            }
        });

        if (!response.data.salePrice) {
            console.error('Error: salePrice is null or undefined');
            return;  // Exit early if salePrice is not valid
        }

        console.log('Sell response:', response.data);

        // Ensure saleProceeds is a valid number
        const saleProceeds = response.data.salePrice * quantityToSell;
        if (isNaN(saleProceeds)) {
            console.error('Invalid sale proceeds:', saleProceeds);
            return;
        }

        // Update balance with the sale proceeds
        await updateBalance(saleProceeds);

        // Refetch the updated portfolio
        await fetchUserPortfolio();  // Ensure this is called after balance update
    } catch (error) {
        console.error('Error selling stock:', error.response ? error.response.data : error.message);
    }
};

const handleQuantityChange = (purchaseId, value) => {
  const maxQuantity = portfolio.purchases.find(p => p.id === purchaseId)?.quantity || 1;
  const sanitizedValue = Math.max(1, Math.min(value, maxQuantity));
  setSellQuantities(prev => ({
    ...prev,
    [purchaseId]: sanitizedValue || 1  // Initialize with default value if not present
  }));
};
  if (!user) {
    return (
      <div className="container">
        <NavBar />
        <h1>User Portfolio</h1>
        <p>Please log in to view your portfolio.</p>
      </div>
    );
  }

  if (loading) {
    return (
      <div className="container">
        <NavBar />
        <h1>User Portfolio</h1>
        <p>Loading portfolio...</p>
      </div>
    );
  }

  return (
    <div className="container">
      <NavBar />
      <h1>User Portfolio</h1>
      <p>Available Balance: ${balance.toFixed(2)}</p>
      <p>Total Investment: ${totalInvestment.toFixed(2)}</p>
      {portfolio ? (
        <>
          {portfolio.purchases.length > 0 ? (
            <table>
              <thead>
                <tr>
                  <th>Symbol</th>
                  <th>Name</th>
                  <th>Purchase Price</th>
                  <th>Quantity</th>
                  <th>Total Value</th>
                  <th>Sell Quantity</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                {portfolio.purchases.map((purchase) => (
                  <tr key={purchase.id}>
                    <td>{purchase.stockSymbol}</td>
                    <td>{purchase.stockName}</td>
                    <td>${purchase.price.toFixed(2)}</td>
                    <td>{purchase.quantity}</td>
                    <td>${(purchase.price * purchase.quantity).toFixed(2)}</td>
                    <td>
                      <input
                        type="number"
                        min="1"
                        max={purchase.quantity}
                        value={sellQuantities[purchase.id] || 1}
                        onChange={(e) => handleQuantityChange(purchase.id, parseInt(e.target.value))}
                      />
                    </td>
                    <td>
                      <button onClick={() => handleSellClick(purchase.stockSymbol, purchase.id)}>
                        Sell
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          ) : (
            <p>You do not own any stocks at the moment.</p>
          )}
        </>
      ) : (
        <p>Error loading portfolio...</p>
      )}
    </div>
  );
};

export default UserPortfolio;