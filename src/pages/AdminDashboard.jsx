import DashboardLayout from "../components/DashboardLayout";
import StatCard from "../components/StatCard";
import UserManagement from "../components/UserManagement";
import DonationMonitoring from "../components/DonationMonitoring";
import ActivityFeed from "../components/ActivityFeed";
import "../components/StatCard.css";

function AdminDashboard() {
  return (
    <DashboardLayout role="Admin">

      <div className="dashboard-intro">
        <h1>Platform Overview</h1>

        <p>
          Monitor food donations, users, and platform activity.
        </p>
      </div>

      <div className="stats-grid">

        <StatCard
          title="Total Users"
          value="248"
          description="Registered platform users"
        />

        <StatCard
          title="Total Donations"
          value="1,284"
          description="Food donations created"
        />

        <StatCard
          title="Food Redistributed"
          value="8,420 kg"
          description="Food successfully redistributed"
        />

        <StatCard
          title="Active Requests"
          value="86"
          description="Requests currently active"
        />

      </div>

      <UserManagement />

      <DonationMonitoring />

      <ActivityFeed />

    </DashboardLayout>
  );
}

export default AdminDashboard;