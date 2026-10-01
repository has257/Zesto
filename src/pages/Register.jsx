import { useState } from "react";
import "./Register.css";

function Register() {
  const [role, setRole] = useState("donor");

  return (
    <div className="register-page">

      <div className="register-container">

        <div className="register-header">
          <h1>Food NGO Platform</h1>
          <p>Join us in reducing food waste.</p>
        </div>

        <div className="register-card">

          <h2>Create Account</h2>

          <p className="register-subtitle">
            Create your account to get started.
          </p>

          <form>

            <div className="form-group">
              <label>Full Name</label>

              <input
                type="text"
                placeholder="Enter your full name"
              />
            </div>

            <div className="form-group">
              <label>Email</label>

              <input
                type="email"
                placeholder="Enter your email"
              />
            </div>

            <div className="form-group">
              <label>Password</label>

              <input
                type="password"
                placeholder="Create a password"
              />
            </div>

            <div className="form-group">
              <label>Confirm Password</label>

              <input
                type="password"
                placeholder="Confirm your password"
              />
            </div>

            <div className="form-group">
              <label>Register as</label>

              <select
                value={role}
                onChange={(e) => setRole(e.target.value)}
              >
                <option value="donor">Donor</option>
                <option value="ngo">NGO</option>
              </select>
            </div>

            <div className="form-group">
              <label>
                {role === "donor"
                  ? "Restaurant / Organization Name"
                  : "NGO Name"}
              </label>

              <input
                type="text"
                placeholder={
                  role === "donor"
                    ? "Enter restaurant or organization name"
                    : "Enter NGO name"
                }
              />
            </div>

            <div className="form-group">
              <label>Phone Number</label>

              <input
                type="tel"
                placeholder="Enter phone number"
              />
            </div>

            <div className="form-group">
              <label>Address</label>

              <textarea
                placeholder="Enter your address"
                rows="3"
              ></textarea>
            </div>

            <button
              type="submit"
              className="register-button"
            >
              Create Account
            </button>

          </form>

          <p className="login-link">
            Already have an account?{" "}
            <a href="/login">Login</a>
          </p>

        </div>

      </div>

    </div>
  );
}

export default Register;