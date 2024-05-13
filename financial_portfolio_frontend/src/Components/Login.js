import axios from "axios";
import React, { useState } from 'react';
import "../CSS/Login.css";
import { useNavigate } from "react-router-dom";

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const navigate = useNavigate("");

  const handleUsernameChange = (event) => {
    setUsername(event.target.value);
  };

  const handlePasswordChange = (event) => {
    setPassword(event.target.value);
  };

  async function handleSubmit(event) {
    event.preventDefault();

    const response = await axios.post("http://localhost:8080/authenticate", {
      username: username,
      password: password
    });

    // Check if response contains username
    if (response.data.username !== undefined) {
      // Authentication successful
      localStorage.setItem("userId", response.data.id);
      localStorage.setItem("username", response.data.username);
      localStorage.setItem("role", response.data.role);
      localStorage.setItem("token", response.data.token);
      navigate("/strategies");
      window.location.reload();
    } else {
      alert("Username or password incorrect");
      localStorage.setItem("username", '');
      localStorage.setItem("role", '');
      localStorage.setItem("token", '');
    }
  };

  return ( 
    <div className="login">
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Username:</label>
          <input
            className="form-control"
            placeholder="Enter username..."
            type="text"
            value={username}
            onChange={handleUsernameChange}
            required
          />
        </div>
        
        <div className="form-group">
          <label>Password:</label>
          <input
            className="form-control"
            placeholder="Enter password..."
            type="password"
            value={password}
            onChange={handlePasswordChange}
            required
          />
        </div>
        <button className="btn btn-primary" type="submit">Login</button>
      </form>
    </div>
   );
}

export default Login;
