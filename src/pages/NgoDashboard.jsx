import { useState } from "react";
import DashboardLayout from "../components/DashboardLayout";
import StatCard from "../components/StatCard";
import AvailableDonations from "../components/AvailableDonations";
import MyRequests from "../components/MyRequests";
import "../components/StatCard.css";

function NgoDashboard() {
  const donations = [
    {
      id: 1,
      foodType: "Cooked Food",
      quantity: "25 kg",
      expiry: "29 Sep 2026, 10:00 PM",
      donor: "Green Leaf Restaurant",
    },
    {
      id: 2,
      foodType: "Bakery Items",
      quantity: "15 kg",
      expiry: "30 Sep 2026, 08:00 AM",
      donor: "Fresh Bakes",
    },
    {
      id: 3,
      foodType: "Fruits & Vegetables",
      quantity: "30 kg",
      expiry: "30 Sep 2026, 06:00 PM",
      donor: "City Supermarket",
    },
    {
      id: 4,
      foodType: "Packaged Food",
      quantity: "40 kg",
      expiry: "02 Oct 2026, 08:00 PM",
      donor: "FoodMart",
    },
  ];

  const [requestedDonations, setRequestedDonations] = useState([]);

  const handleRequest = (donation) => {
    setRequestedDonations((previousRequests) => {
      if (
        previousRequests.some(
          (request) => request.id === donation.id
        )
      ) {
        return previousRequests;
      }

      return [
        ...previousRequests,
        {
          ...donation,
          requestedOn: "29 Sep 2026",
          status: "Pending",
        },
      ];
    });
  };

  const availableDonationCount =
    donations.length - requestedDonations.length;

  const activeRequestCount =
    requestedDonations.filter(
      (request) => request.status === "Pending"
    ).length;

  return (
    <DashboardLayout role="NGO">

      <div className="dashboard-intro">
        <h1>Welcome back!</h1>

        <p>
          Find available food donations and manage your requests.
        </p>
      </div>

      <div className="stats-grid">

        <StatCard
          title="Available Donations"
          value={availableDonationCount}
          description="Food donations available"
        />

        <StatCard
          title="Active Requests"
          value={activeRequestCount}
          description="Requests awaiting action"
        />

        <StatCard
          title="Food Received"
          value="142 kg"
          description="Total food received"
        />

        <StatCard
          title="People Served"
          value="315"
          description="Estimated people helped"
        />

      </div>

      <AvailableDonations
        donations={donations}
        requestedDonations={requestedDonations}
        onRequest={handleRequest}
      />

      <MyRequests
        requests={requestedDonations}
      />

    </DashboardLayout>
  );
}

export default NgoDashboard;