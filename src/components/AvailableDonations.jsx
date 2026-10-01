import DonationCard from "./DonationCard";
import "./AvailableDonations.css";

function AvailableDonations({
  donations,
  requestedDonations,
  onRequest,
}) {
  return (
    <div className="available-donations">

      <div className="available-donations-header">
        <h2>Available Food Donations</h2>

        <p>
          Browse food donations available for pickup.
        </p>
      </div>

      <div className="donation-cards-grid">

        {donations.map((donation) => (
          <DonationCard
            key={donation.id}
            foodType={donation.foodType}
            quantity={donation.quantity}
            expiry={donation.expiry}
            donor={donation.donor}
            isRequested={requestedDonations.some(
              (request) => request.id === donation.id
            )}
            onRequest={() => onRequest(donation)}
          />
        ))}

      </div>

    </div>
  );
}

export default AvailableDonations;