import React, { createContext, useContext, useState} from 'react';
import axios from 'axios';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  const login = async (username, password) => {
    try {
      const response = await axios.post('http://localhost:8080/api/login', 
        { username, password },
        { headers: { 'Content-Type': 'application/json' } }
      );
      console.log('Login response:', response.data);
      console.log('Login response:', response.data);

      // Set user data with ID converted to a string
      setUser({ 
        id: String(response.data.userId), // Convert Long to String
        username: response.data.username 
      });
      return true;
    } catch (error) {
      console.error('Login error:', error.response ? error.response.data : error);
      return false;
    }
  };



  const logout = async () => {
    try {
      await axios.post('http://localhost:8080/api/logout', {}, { withCredentials: true });
      setUser(null);
    } catch (error) {
      console.error('Logout error:', error);
    }
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);