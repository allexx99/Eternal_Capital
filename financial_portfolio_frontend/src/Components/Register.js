// Register.js
import axios from "axios";
import React, { useState } from 'react';
import "../CSS/Register.css";
import { useNavigate } from "react-router-dom";

const Register = () => {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const navigate = useNavigate("");

  const handleFirstNameChange = (event) => {
    setFirstName(event.target.value);
  };

  const handleLastNameChange = (event) => {
    setLastName(event.target.value);
  };

  const handleEmailChange = (event) => {
    setEmail(event.target.value);
  };

  const handleUsernameChange = (event) => {
    setUsername(event.target.value);
  };

  const handlePasswordChange = (event) => {
    setPassword(event.target.value);
  };

  async function handleSubmit(event) {
    event.preventDefault();
    
    const response = await axios.post("http://localhost:8080/register", {
      firstName: firstName,
      lastName: lastName,
      email: email,
      username: username,
      password: password
    });

    // Check if response contains username
    if (response.data.username !== undefined) {
      // Authentication successful
      localStorage.setItem("username", response.data.username);
      localStorage.setItem("role", response.data.role);
      localStorage.setItem("token", response.data.token);
      navigate("/");
      window.location.reload();
    } else {
      alert("Error on registering user");
      localStorage.setItem("username", '');
      localStorage.setItem("role", '');
      localStorage.setItem("token", '');
    }
  };

  return ( 
    <div className="register">
      <h2>Register</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>First Name:</label>
          <input
            className="form-control"
            type="text"
            value={firstName}
            onChange={handleFirstNameChange}
            required
          />
        </div>
        <div className="form-group">
          <label>Last Name:</label>
          <input
            className="form-control"
            type="text"
            value={lastName}
            onChange={handleLastNameChange}
            required
          />
        </div>
        <div className="form-group">
          <label>Email:</label>
          <input
            className="form-control"
            type="email"
            value={email}
            onChange={handleEmailChange}
            required
          />
        </div>
        <div className="form-group">
          <label>Username:</label>
          <input
            className="form-control"
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
            type="password"
            value={password}
            onChange={handlePasswordChange}
            required
          />
        </div>
        <button className="btn btn-primary" type="submit">Register</button>
      </form>
    </div>
   );
}

export default Register;
