import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import './Homepage.css';
import { PaymentContext } from './PaymentContext'; 
import NavBar from './Navbar';
import axios from 'axios';

import { useAuth } from './AuthProvider';

const HomePage = () => {
    const navigate = useNavigate();
    const { balance } = useContext(PaymentContext);
    const { logout } = useAuth();
    const [stocks, setStocks] = useState([]);
    const [loading, setLoading] = useState(false);

    // Fetch stock data for multiple companies
    useEffect(() => {
        const fetchStockData = async () => {
            setLoading(true);
            const storedStocks = localStorage.getItem('stockData');
            
            if (storedStocks) {
                // If there's data in local storage, use it
                setStocks(JSON.parse(storedStocks));
                setLoading(false);
                return;
            }
    
            try {
                const response = await axios.get('http://localhost:8080/api/stocks/details/multiple?symbols=AAPL,GOOGL,IBM,AMZN,COF,MSFT,RKLB,SOFI,NVDA,NFLX,PYPL,UBER,ABNB,ZM,DBX,SNAP,PINS');
                setStocks(response.data);
                localStorage.setItem('stockData', JSON.stringify(response.data)); // Save to local storage
            } catch (error) {
                console.error('Error fetching stock data:', error);
            } finally {
                setLoading(false);
            }
        };
    
        fetchStockData();
    }, []);

    const handleBuyClick = (stockSymbol) => {
        navigate(`/buy/${stockSymbol}`);
    };

    


    const handleLogout = async () => {
        try {
          // Call the logout endpoint
          await axios.post('http://localhost:8080/api/logout'); 
          logout();
         
          // Clear any user data from context or local storage
          // For example: localStorage.removeItem('user');
          console.log('Logged out successfully');
          navigate('/'); // Redirect to login page after logout
        } catch (error) {
          console.error('Logout error:', error);
        }
      };
      const { user } = useAuth(); 

    return (
        <div className="container">
        <div className='nav'><NavBar /></div>
        {user ? <p>You are logged in as: {user.username}</p> : <p>Please log in.</p>}
        <button onClick={handleLogout}>Logout</button>
        <h1>Stock List</h1>
        <p className="balance">Available Balance: ${balance.toFixed(2)}</p>

        {stocks.length > 0 ? (
            <table>
                <thead>
                    <tr>
                        <th>Symbol</th>
                        <th>Name</th>
                        <th>Open Price</th>
                        <th>Current Price</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    {stocks.map(stock => (
                        <tr key={stock.symbol}>
                            <td>{stock.symbol}</td>
                            <td>{stock.name}</td>
                            <td className="price-cell">${Number(stock.openPrice).toFixed(2)}</td>
                            <td className="price-cell">${Number(stock.currentPrice).toFixed(2)}</td>
                            <td>
                                <button onClick={() => handleBuyClick(stock.symbol)}>Buy</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        ) : (
            <p>No stock data available</p>
        )}

        <div className="load-more">
            {loading ? <p>Loading...</p> : null}
        </div>
    </div>
    );
};

export default HomePage;