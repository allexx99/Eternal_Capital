// AuthPage.js
import React, { useState } from "react";
import Login from "./Login";
import Register from "./Register";
import Video from "./Video";
import "../CSS/AuthPage.css";

const AuthPage = () => {
  const [showLoginForm, setShowLoginForm] = useState(true);

  const handleLoginClick = () => {
    setShowLoginForm(true);
  };

  const handleRegisterClick = () => {
    setShowLoginForm(false);
  };

  return (
    <div className="auth-page-container">
      {/* Left half */}
      <div className="left-half">
        <div className="left-half-button">
          <button className="btn btn-primary" onClick={handleLoginClick}>Login</button>
          <button className="btn btn-primary" onClick={handleRegisterClick}>Register</button>
        </div>
        {showLoginForm ? <Login /> : <Register />}
      </div>

      {/* Right half */}
      <div className="right-half">
        <Video />
      </div>
    </div>
  );
};

export default AuthPage;
