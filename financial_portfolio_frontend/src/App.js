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
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;
