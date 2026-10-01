import DashboardLayout from "../components/DashboardLayout";
import StatCard from "../components/StatCard";
import DonationForm from "../components/DonationForm";
import DonationList from "../components/DonationList";
import "../components/StatCard.css";

function DonorDashboard() {
  return (
    <DashboardLayout role="Donor">

      <div className="dashboard-intro">
        <h1>Welcome back!</h1>
        <p>
          Manage your food donations and see their impact.
        </p>
      </div>

      <div className="stats-grid">

        <StatCard
          title="Total Donations"
          value="24"
          description="Donations made"
        />

        <StatCard
          title="Food Donated"
          value="186 kg"
          description="Total food contributed"
        />

        <StatCard
          title="People Served"
          value="420"
          description="Estimated people helped"
        />

        <StatCard
          title="Successful Pickups"
          value="21"
          description="Donations collected"
        />

      </div>

      <DonationForm />

      <DonationList />

    </DashboardLayout>
  );
}

export default DonorDashboard;