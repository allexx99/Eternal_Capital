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
  const [errors, setErrors] = useState({});

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

  const validateForm = () => {
    let formErrors = {};
    let isValid = true;

    if (!email) {
      isValid = false;
      formErrors["email"] = "Email is required";
    } else {
      const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailPattern.test(email)) {
        isValid = false;
        formErrors["email"] = "Invalid email format";
      }
    }

    if (!password) {
      isValid = false;
      formErrors["password"] = "Password is required";
    } else if (password.length < 8) {
      isValid = false;
      formErrors["password"] = "Password must be at least 8 characters";
    } else if (!/[A-Z]/.test(password)) {
      isValid = false;
      formErrors["password"] = "Password must contain at least one uppercase letter";
    } else if (!/[a-z]/.test(password)) {
      isValid = false;
      formErrors["password"] = "Password must contain at least one lowercase letter";
    } else if (!/[0-9]/.test(password)) {
      isValid = false;
      formErrors["password"] = "Password must contain at least one number";
    } else if (!/[!@#$%^&*]/.test(password)) {
      isValid = false;
      formErrors["password"] = "Password must contain at least one special character";
    }

    setErrors(formErrors);
    return isValid;
  };

  async function handleSubmit(event) {
    event.preventDefault();

    if (validateForm()) {
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
        localStorage.setItem("userId", response.data.id);
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
    }
  };

  return (
    <div className="register">
      <h2>Register</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>First Name:</label>
          <input
            className={`form-control ${errors.firstName ? 'is-invalid' : ''}`}
            type="text"
            value={firstName}
            onChange={handleFirstNameChange}
            required
          />
          {errors.firstName && <div className="invalid-feedback">{errors.firstName}</div>}
        </div>
        <div className="form-group">
          <label>Last Name:</label>
          <input
            className={`form-control ${errors.lastName ? 'is-invalid' : ''}`}
            type="text"
            value={lastName}
            onChange={handleLastNameChange}
            required
          />
          {errors.lastName && <div className="invalid-feedback">{errors.lastName}</div>}
        </div>
        <div className="form-group">
          <label>Email:</label>
          <input
            className={`form-control ${errors.email ? 'is-invalid' : ''}`}
            type="email"
            value={email}
            onChange={handleEmailChange}
            required
          />
          {errors.email && <div className="invalid-feedback">{errors.email}</div>}
        </div>
        <div className="form-group">
          <label>Username:</label>
          <input
            className={`form-control ${errors.username ? 'is-invalid' : ''}`}
            type="text"
            value={username}
            onChange={handleUsernameChange}
            required
          />
          {errors.username && <div className="invalid-feedback">{errors.username}</div>}
        </div>
        <div className="form-group">
          <label>Password:</label>
          <input
            className={`form-control ${errors.password ? 'is-invalid' : ''}`}
            type="password"
            value={password}
            onChange={handlePasswordChange}
            required
          />
          {errors.password && <div className="invalid-feedback">{errors.password}</div>}
        </div>
        <button className="btn btn-primary" type="submit">Register</button>
      </form>
    </div>
  );
}

export default Register;
