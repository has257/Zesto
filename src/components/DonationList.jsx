import "./DonationList.css";

function DonationList() {
  const donations = [
    {
      id: 1,
      foodType: "Cooked Food",
      quantity: "25 kg",
      expiry: "29 Sep 2026, 10:00 PM",
      status: "Available",
    },
    {
      id: 2,
      foodType: "Bakery Items",
      quantity: "15 kg",
      expiry: "30 Sep 2026, 08:00 AM",
      status: "Requested",
    },
    {
      id: 3,
      foodType: "Fruits & Vegetables",
      quantity: "30 kg",
      expiry: "30 Sep 2026, 06:00 PM",
      status: "Collected",
    },
  ];

  return (
    <div className="donation-list-card">

      <div className="donation-list-header">
        <div>
          <h2>My Donations</h2>
          <p>Track your recent food donations.</p>
        </div>
      </div>

      <div className="donation-table">

        <div className="donation-table-header">
          <span>Food Type</span>
          <span>Quantity</span>
          <span>Best Before</span>
          <span>Status</span>
        </div>

        {donations.map((donation) => (
          <div
            className="donation-table-row"
            key={donation.id}
          >
            <span>{donation.foodType}</span>

            <span>{donation.quantity}</span>

            <span>{donation.expiry}</span>

            <span>
              <span
                className={`status-badge status-${donation.status.toLowerCase()}`}
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

export default DonationList;