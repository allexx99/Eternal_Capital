import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import "../CSS/Navbar.css";

const Navbar = () => {
  const username = localStorage.getItem("username");

  const truncatedUsername = username && username.length > 11 ? `${username.slice(0, 11)}...` : username;

  const navigate = useNavigate("");

  const handleLogout = () => {
    // Logic for logging out, e.g., clearing local storage
    localStorage.setItem("username", '');
    localStorage.setItem("role", '');
    localStorage.setItem("token", '');
    localStorage.setItem("userId", '');
    // Other logout actions
    navigate("/");
    window.location.reload();
  };

  return (
    <nav className="navbar">
      <div className="title">
        <div className="logo-image">
          <img src="/dollar_sign.jpg" alt="Logo Image" />
        </div>

        <div className="title-text">
          <h1>Eternal Capital</h1>
        </div>
      </div>
      

      <div className="links">
        <Link to="/">Home</Link>
        {/* <Link>Features</Link>
        <Link>Pricing</Link>
        <Link>About</Link> */}
        <a className="nav-link" href="#features">Features</a>
        <a className="nav-link" href="#pricing">Pricing</a>
        <a className="nav-link" href="#about">About</a>
      </div>
      {username ? (
        <div className="dropdown">
          <button className="dropbtn">{truncatedUsername}</button>
          <div className="dropdown-content">
            <Link to="/profile">Profile</Link>
            <Link to="/my-portfolios">My portfolios</Link>
            <Link to="/strategies">Strategies</Link>
            <button onClick={handleLogout}>Logout</button>
          </div>
        </div>
      ) : (
        <Link to="/auth" className="btn btn-primary">Get started</Link>
      )}
    </nav>
    
  );
};

export default Navbar;
