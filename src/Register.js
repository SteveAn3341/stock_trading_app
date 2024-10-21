
import './LoginPagemodule.css';  // Link to the CSS file
import { Link , useNavigate} from 'react-router-dom';
import React, { useState } from 'react';
import axios from 'axios'; 



  
const Register = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();  // Use navigate to redirect after registration

  const handleRegister = async (event) => {
    event.preventDefault();

    try {
      // Send registration request to backend
      const response = await axios.post('http://localhost:8080/register', {
        username: email,  // Assuming backend expects 'username'
        password: password,
      });

      // Handle successful registration (redirect to login page)
      console.log('Registration successful:', response.data);
      navigate('/login');  // Redirect to login page after successful registration
    } catch (error) {
      // Handle registration failure (show error message, etc.)
      console.error('Registration error:', error);
    }
  };

  return (
    <div className="login-container">
      <div className="login-box">
        <Link to="/">Home</Link>
        <h1>WELCOME</h1>
        <form onSubmit={handleRegister}>
          <div className="input-group">
            <label htmlFor="Email">Email</label>
            <input
              type="text"
              name="Email"
              id="Email"
              placeholder="Enter Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}  // Update email state
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
              onChange={(e) => setPassword(e.target.value)}  // Update password state
            />
          </div>
          
          <button type="submit" className="login-btn">Register</button>
        </form>
      </div>
    </div>
  );
};

export default Register;