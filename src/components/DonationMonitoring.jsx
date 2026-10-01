import "./DonationMonitoring.css";

function DonationMonitoring() {
  const donations = [
    {
      id: 1,
      foodType: "Cooked Food",
      quantity: "25 kg",
      donor: "Green Leaf Restaurant",
      ngo: "Helping Hands NGO",
      status: "Collected",
    },
    {
      id: 2,
      foodType: "Bakery Items",
      quantity: "15 kg",
      donor: "Fresh Bakes",
      ngo: "Hope Foundation",
      status: "Requested",
    },
    {
      id: 3,
      foodType: "Fruits & Vegetables",
      quantity: "30 kg",
      donor: "City Supermarket",
      ngo: "Not Assigned",
      status: "Available",
    },
    {
      id: 4,
      foodType: "Packaged Food",
      quantity: "40 kg",
      donor: "FoodMart",
      ngo: "Helping Hands NGO",
      status: "Collected",
    },
  ];

  return (
    <div className="donation-monitoring">

      <div className="donation-monitoring-header">
        <h2>Donation Monitoring</h2>

        <p>
          Monitor food donations and their current status.
        </p>
      </div>

      <div className="monitoring-table">

        <div className="monitoring-table-header">
          <span>Food Type</span>
          <span>Quantity</span>
          <span>Donor</span>
          <span>NGO</span>
          <span>Status</span>
        </div>

        {donations.map((donation) => (
          <div
            className="monitoring-table-row"
            key={donation.id}
          >
            <span>{donation.foodType}</span>

            <span>{donation.quantity}</span>

            <span>{donation.donor}</span>

            <span>{donation.ngo}</span>

            <span>
              <span
                className={`monitoring-status monitoring-status-${donation.status
                  .toLowerCase()
                  .replace(" ", "-")}`}
              >
                {donation.status}
              </span>
            </span>
          </div>
        ))}

      </div>

    </div>
  );
}

export default DonationMonitoring;