// import React, { useState, useContext } from 'react';
// import { PaymentContext } from './PaymentContext';  // Import the context
// import Navbar from './Navbar';
// import './Payment.css';

// const PaymentPage = () => {
//   const [cardNumber, setCardNumber] = useState('');
//   const [expiryDate, setExpiryDate] = useState('');
//   const [cvv, setCvv] = useState('');
//   const [amount, setAmount] = useState('');
//   const { balance } = useContext(PaymentContext);
//   const { updateBalance } = useContext(PaymentContext);  // Access updateBalance from context

//   const handlePaymentSubmit = (e) => {
//     e.preventDefault();
//     updateBalance(parseFloat(amount));  // Update the balance with the payment amount
//     alert(`Payment of $${amount} made using card ending in ${cardNumber.slice(-4)}.`);
//   };

//   return (
//     <div>
//       <Navbar />
//       <div className="container">
//         <h1>Enter Payment Details</h1>
//         <p>Available Balance: ${balance.toFixed(2)}</p>
//         <form onSubmit={handlePaymentSubmit}>
//           <div className="form-group">
//             <label htmlFor="cardNumber">Card Number:</label>
//             <input
//               type="text"
//               id="cardNumber"
//               value={cardNumber}
//               onChange={(e) => setCardNumber(e.target.value)}
//               placeholder="Enter card number"
//               maxLength="16"
//               required
//             />
//           </div>
//           <div className="form-group">
//             <label htmlFor="expiryDate">Expiry Date (MM/YY):</label>
//             <input
//               type="text"
//               id="expiryDate"
//               value={expiryDate}
//               onChange={(e) => setExpiryDate(e.target.value)}
//               placeholder="MM/YY"
//               maxLength="5"
//               required
//             />
//           </div>
//           <div className="form-group">
//             <label htmlFor="cvv">CVV:</label>
//             <input
//               type="text"
//               id="cvv"
//               value={cvv}
//               onChange={(e) => setCvv(e.target.value)}
//               placeholder="Enter CVV"
//               maxLength="3"
//               required
//             />
//           </div>
//           <div className="form-group">
//             <label htmlFor="amount">Amount ($):</label>
//             <input
//               type="number"
//               id="amount"
//               value={amount}
//               onChange={(e) => setAmount(e.target.value)}
//               placeholder="Enter amount"
//               required
//             />
//           </div>
//           <button type="submit">Submit Payment</button>
//         </form>
//       </div>
//     </div>
//   );
// };

// export default PaymentPage;


import React, { useState, useEffect, useContext, useCallback } from 'react';
import NavBar from './Navbar';
import axios from 'axios';
import { useAuth } from './AuthProvider';
import { PaymentContext } from './PaymentContext';

const UserPortfolio = () => {
    const [portfolio, setPortfolio] = useState(null);
    const [sellQuantities, setSellQuantities] = useState({});
    const { balance, updateBalance } = useContext(PaymentContext);
    const { user } = useAuth();
    const [totalInvestment, setTotalInvestment] = useState(0);

    const fetchUserPortfolio = useCallback(async () => {
        if (!user) return;
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
            console.error('Error fetching portfolio:', error);
        }
    }, [user]);

    useEffect(() => {
        if (user) {
            fetchUserPortfolio();
        }
    }, [user, fetchUserPortfolio]);

    const handleSellClick = async (symbol, purchaseId) => {
        if (!user) return;
        const quantityToSell = sellQuantities[purchaseId];
        try {
            console.log(`Attempting to sell ${quantityToSell} shares of ${symbol} for user: ${user.id}`);
            const response = await axios.post(`http://localhost:8080/api/portfolio/sell/${symbol}`, null, {
                params: { userId: user.id, quantity: quantityToSell }
            });
            console.log('Sell response:', response.data);
            
            // Update balance with the sale proceeds
            const saleProceeds = response.data.salePrice * quantityToSell; // Ensure your API returns salePrice
            await updateBalance(saleProceeds);

            // Update portfolio and total investment
            fetchUserPortfolio(); // Refetch the updated portfolio
        } catch (error) {
            console.error('Error selling stock:', error.response ? error.response.data : error.message);
        }
    };

    const handleQuantityChange = (purchaseId, value) => {
        setSellQuantities(prev => ({
            ...prev,
            [purchaseId]: Math.max(1, Math.min(value, portfolio.purchases.find(p => p.id === purchaseId).quantity))
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
                <p>Loading portfolio...</p>
            )}
        </div>
    );
};

export default UserPortfolio;