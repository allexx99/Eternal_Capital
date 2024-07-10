import { BrowserRouter as Router, Route, Routes, Navigate } from "react-router-dom";
import "./App.css";
import Navbar from "./Components/Navbar";
import Home from "./Components/Home";
import Register from "./Components/Register";
import Login from "./Components/Login";
import AuthPage from "./Components/AuthPage";
import Strategies from "./Components/Strategies";
import BETReplication from "./Components/BETReplication";
import FinancialCalculator from "./Components/FinancialCalculator";
import MyPortfolios from "./Components/MyPortfolios";
import FutureValue from "./Components/FutureValue";
import MonthlySavings from "./Components/MonthlySavings";
import MonteCarlo from "./Components/MonteCarlo";
import UserProfile from "./Components/UserProfile";
import Admin from "./Components/Admin";

function App() {

  const isLoggedIn = !!localStorage.getItem('username'); // Check if user is logged in

  // Custom PrivateRoute component to handle protected routes
  const PrivateRoute = ({ element, ...rest }) => {
    return isLoggedIn ? (
      element
    ) : (
      <Navigate to="/auth" />
    )
  };

  const AdminRoute = ({ element, ...rest }) => {
    return localStorage.getItem('role') === 'ADMIN' ? (
      element
    ) : (
      <Navigate to="/auth" />
    )
  }

  return (
    <Router>
      <div className="App">
        <Navbar />
        <div className="content">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/register" element={<Register />} />
            <Route path="/login" element={<Login />} />
            <Route path="/auth" element={<AuthPage />} />
            {/* <Route path="/strategies" element={<Strategies />} /> */}
            <Route path="/strategies" element={<PrivateRoute element={<Strategies />} />} />
            <Route path="/strategies/bet-replication" element={<PrivateRoute element={<BETReplication />} />} />
            <Route path="/strategies/financial-calculator" element={<PrivateRoute element={<FinancialCalculator />} />} />
            <Route path="/my-portfolios" element={<PrivateRoute element={<MyPortfolios />} />} />
            <Route path="/strategies/financial-calculator/future-value" element={<PrivateRoute element={<FutureValue />} />} />
            <Route path="/strategies/financial-calculator/monthly-savings" element={<PrivateRoute element={<MonthlySavings />} />} />
            <Route path="/strategies/financial-calculator/monte-carlo" element={<PrivateRoute element={<MonteCarlo />} />} />
            <Route path="/user-profile" element={<PrivateRoute element={<UserProfile />} />} />
            <Route path="/admin" element={<AdminRoute element={<Admin />} />} />
            {/* <Route path="/admin" element={<Admin />} /> */}
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;
