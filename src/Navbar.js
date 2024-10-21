import React from 'react';
import { Link } from 'react-router-dom';
import './Navbar.css'; // Optional: for styling your navigation bar

const NavBar = () => {
  return (
    <nav className="navbar">
      <ul>
        <li><Link to="/">Home</Link></li>
        <li><Link to="/login">Login</Link></li>
        <li><Link to="/portfolio">User Portfolio</Link></li>
        {/* <li><Link to="/history">History</Link></li> */}
        <li><Link to="/payment">Payment</Link></li>
      </ul>
    </nav>
  );
};

export default NavBar;