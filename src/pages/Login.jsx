import "./Login.css";

function Login() {
  return (
    <div className="login-page">

      <div className="login-container">

        <div className="login-header">
          <h1>Food NGO Platform</h1>
          <p>Connecting surplus food with those who need it.</p>
        </div>

        <div className="login-card">

          <h2>Welcome Back</h2>
          <p className="login-subtitle">
            Login to continue
          </p>

          <form>

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
                placeholder="Enter your password"
              />
            </div>

            <div className="form-group">
              <label>Login as</label>

              <select>
                <option value="donor">Donor</option>
                <option value="ngo">NGO</option>
                <option value="admin">Admin</option>
              </select>

            </div>

            <button type="submit" className="login-button">
              Login
            </button>

          </form>

          <p className="register-link">
            Don't have an account? <a href="/register">Register</a>
          </p>

        </div>

      </div>

    </div>
  );
}

export default Login;