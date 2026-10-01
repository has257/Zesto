import { NavLink } from "react-router-dom";
import "./DashboardLayout.css";

function DashboardLayout({ children, role }) {
  return (
    <div className="dashboard-layout">

      <aside className="sidebar">

        <div className="sidebar-logo">
          FoodRescue
        </div>

        <div className="sidebar-role">
          {role} Portal
        </div>

        <nav className="sidebar-nav">

          <NavLink
            to={`/${role.toLowerCase()}`}
            className={({ isActive }) =>
              isActive ? "active" : ""
            }
          >
            <span className="nav-icon">⌂</span>
            Dashboard
          </NavLink>

          <NavLink to="#" className="disabled-nav">
            <span className="nav-icon">▣</span>
            Donations
          </NavLink>

          <NavLink to="#" className="disabled-nav">
            <span className="nav-icon">◷</span>
            Requests
          </NavLink>

          <NavLink to="#" className="disabled-nav">
            <span className="nav-icon">↗</span>
            Impact
          </NavLink>

          <NavLink to="#" className="disabled-nav">
            <span className="nav-icon">●</span>
            Profile
          </NavLink>

        </nav>

        <div className="sidebar-bottom">

          <NavLink to="/login">
            <span className="nav-icon">↪</span>
            Logout
          </NavLink>

        </div>

      </aside>

      <main className="dashboard-content">

        <header className="dashboard-header">

          <h2>{role} Dashboard</h2>

          <div className="user-info">
            Welcome, {role}
          </div>

        </header>

        <section className="dashboard-main">
          {children}
        </section>

      </main>

    </div>
  );
}

export default DashboardLayout;