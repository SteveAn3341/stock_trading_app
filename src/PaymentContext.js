// import React, { createContext, useContext, useState } from 'react';

// // Create a Context for managing balance
// export const PaymentContext = createContext();

// // Create a Provider component to wrap the app
// export const PaymentProvider = ({ children }) => {
//     const [balance, setBalance] = useState(() => {
//         const savedBalance = localStorage.getItem('balance');
//         return savedBalance ? parseFloat(savedBalance) : 0; 
//     });

//     const updateBalance = (amount) => {
//         setBalance(prevBalance => {
//             const newBalance = prevBalance + amount;
//             localStorage.setItem('balance', newBalance);
//             return newBalance;
//         });
//     };

//     return (
//         <PaymentContext.Provider value={{ balance, updateBalance }}>
//             {children}
//         </PaymentContext.Provider>
//     );
// };

// // Create and export the usePayment hook
// export const usePayment = () => useContext(PaymentContext);



import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import { useAuth } from './AuthProvider';

export const PaymentContext = createContext();

export const PaymentProvider = ({ children }) => {
    const [balance, setBalance] = useState(0);
    const [loading, setLoading] = useState(true);
    const { user } = useAuth();

    const fetchBalance = useCallback(async () => {
        if (!user) return;
        try {
            setLoading(true);
            const response = await axios.get(`http://localhost:8080/api/user/${user.id}/balance`);
            console.log('Balance response:', response.data);
            setBalance(response.data.balance);
        } catch (error) {
            console.error('Error fetching balance:', error.response ? error.response.data : error.message);
        } finally {
            setLoading(false);
        }
    }, [user]);

    useEffect(() => {
        fetchBalance();
    }, [fetchBalance]);

    const updateBalance = useCallback(async (amount) => {
      if (!user) return;
      try {
          setLoading(true);
          // Sending the amount in the request body, not as a path parameter
          const response = await axios.post(`http://localhost:8080/api/user/${user.id}/balance`, { amount });
          setBalance(response.data.balance); // Assuming the response contains the updated balance
      } catch (error) {
          console.error('Error updating balance:', error);
          throw error;
      } finally {
          setLoading(false);
      }
  }, [user]);

    const contextValue = {
        balance,
        updateBalance,
        loading,
        refreshBalance: fetchBalance
    };

    return (
        <PaymentContext.Provider value={contextValue}>
            {children}
        </PaymentContext.Provider>
    );
};

export const usePayment = () => useContext(PaymentContext);