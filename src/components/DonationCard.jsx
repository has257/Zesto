import "./DonationCard.css";

function DonationCard({
  foodType,
  quantity,
  expiry,
  donor,
  isRequested,
  onRequest,
}) {
  return (
    <div className="donation-card">

      <div className="donation-card-header">

        <h3>{foodType}</h3>

        <span className="donation-quantity">
          {quantity}
        </span>

      </div>

      <div className="donation-card-details">

        <p>
          <strong>Best before:</strong> {expiry}
        </p>

        <p>
          <strong>Donor:</strong> {donor}
        </p>

      </div>

      <button
        className={`request-food-button ${
          isRequested ? "requested" : ""
        }`}
        onClick={onRequest}
        disabled={isRequested}
      >
        {isRequested ? "Requested" : "Request Food"}
      </button>

    </div>
  );
}

export default DonationCard;