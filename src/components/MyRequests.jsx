import "./MyRequests.css";

function MyRequests({ requests }) {
  return (
    <div className="my-requests">

      <div className="my-requests-header">
        <h2>My Requests</h2>

        <p>
          Track the food donations you have requested.
        </p>
      </div>

      {requests.length === 0 ? (
        <div className="no-requests">
          <p>
            You haven't requested any food donations yet.
          </p>
        </div>
      ) : (
        <div className="requests-table">

          <div className="requests-table-header">
            <span>Food Type</span>
            <span>Quantity</span>
            <span>Donor</span>
            <span>Requested On</span>
            <span>Status</span>
          </div>

          {requests.map((request) => (
            <div
              className="requests-table-row"
              key={request.id}
            >
              <span>{request.foodType}</span>

              <span>{request.quantity}</span>

              <span>{request.donor}</span>

              <span>{request.requestedOn}</span>

              <span>
                <span
                  className={`request-status request-status-${request.status.toLowerCase()}`}
                >
                  {request.status}
                </span>
              </span>
            </div>
          ))}

        </div>
      )}

    </div>
  );
}

export default MyRequests;