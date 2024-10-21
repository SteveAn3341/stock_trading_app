// import React, { useState } from 'react';
// import Navbar from './Navbar'; // Import NavBar for navigation
// import './History.css';
// import { PaymentContext } from './PaymentContext'; 
// import  { useContext } from 'react';
// const HistoryPage = () => {
//   // Sample transaction history (this could come from a database or API in a real app)
//   const [history] = useState([
//     { id: 1, date: '2024-10-14', symbol: 'APPL', action: 'Buy', price: 150.23, quantity: 10 },
//     { id: 2, date: '2024-10-15', symbol: 'GOOGL', action: 'Sell', price: 2734.87, quantity: 5 },
//     { id: 3, date: '2024-10-15', symbol: 'AMZN', action: 'Buy', price: 3441.85, quantity: 2 },
//   ]);
//   const { balance } = useContext(PaymentContext);

//   return (
//     <div>
//       <Navbar /> {/* Include the NavBar for consistent navigation */}
//       <div className="container">
//         <h1>Transaction History</h1>
//         <p>Available Balance: ${balance.toFixed(2)}</p>
//         {history.length > 0 ? (
//           <table>
//             <thead>
//               <tr>
//                 <th>Date</th>
//                 <th>Symbol</th>
//                 <th>Action</th>
//                 <th>Price</th>
//                 <th>Quantity</th>
//               </tr>
//             </thead>
//             <tbody>
//               {history.map((transaction) => (
//                 <tr key={transaction.id}>
//                   <td>{transaction.date}</td>
//                   <td>{transaction.symbol}</td>
//                   <td>{transaction.action}</td>
//                   <td>${transaction.price.toFixed(2)}</td>
//                   <td>{transaction.quantity}</td>
//                 </tr>
//               ))}
//             </tbody>
//           </table>
//         ) : (
//           <p>No transaction history available.</p>
//         )}
//       </div>
//     </div>
//   );
// };

// export default HistoryPage;