import { Link } from "react-router-dom";
import "./Navbar.css";

function Navbar() {
  return (
    <nav className="navbar">

      <div className="navbar-container">

        <Link to="/" className="navbar-logo">
          FoodRescue
        </Link>

        <div className="navbar-links">

          <Link to="/">Home</Link>

          <Link to="/login">Login</Link>

          <Link to="/register">Register</Link>

        </div>

      </div>

    </nav>
  );
}

export default Navbar;