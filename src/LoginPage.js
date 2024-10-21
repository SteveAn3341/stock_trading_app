import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from './AuthProvider'; // Use the AuthProvider context
import './LoginPagemodule.css';

const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();
  const { login } = useAuth(); // Destructure login from AuthContext

  const handleLogin = async (e) => {
    e.preventDefault(); // Prevent the default form submission
    const success = await login(email, password); // Use the login function

    if (success) {
      navigate('/'); // Redirect on successful login
    } else {
      // Optionally show an error message
      console.error('Login failed');
    }
  };

  return (
    <div className="login-container">
      <div className="login-box">
        <Link to="/">Home</Link>
        <h1>WELCOME</h1>

        <form onSubmit={handleLogin}>
          <div className="input-group">
            <label htmlFor="Email">Email</label>
            <input
              type="text"
              name="Email"
              id="Email"
              placeholder="Enter Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>
          
          <div className="input-group">
            <label htmlFor="Password">Password</label>
            <input
              type="password"
              name="Password"
              id="Password"
              placeholder="Enter Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>
          
          <button type="submit" className="login-btn">LOGIN</button>
        </form>
          
        <p className="signup">Don't have an account? <Link to="/register">Sign Up </Link></p>
      </div>
    </div>
  );
}

export default LoginPage;