import "./ActivityFeed.css";

function ActivityFeed() {
  const activities = [
    {
      id: 1,
      type: "Donation",
      message: "Green Leaf Restaurant added a new food donation.",
      time: "10 minutes ago",
    },
    {
      id: 2,
      type: "Request",
      message: "Helping Hands NGO requested Cooked Food.",
      time: "25 minutes ago",
    },
    {
      id: 3,
      type: "Registration",
      message: "Hope Foundation registered as a new NGO.",
      time: "1 hour ago",
    },
    {
      id: 4,
      type: "Collection",
      message: "Packaged Food donation was successfully collected.",
      time: "2 hours ago",
    },
    {
      id: 5,
      type: "Donation",
      message: "Fresh Bakes added a new Bakery Items donation.",
      time: "3 hours ago",
    },
  ];

  return (
    <div className="activity-feed">

      <div className="activity-feed-header">
        <h2>Recent Activity</h2>

        <p>
          Latest activity across the platform.
        </p>
      </div>

      <div className="activity-list">

        {activities.map((activity) => (
          <div
            className="activity-item"
            key={activity.id}
          >

            <div className="activity-icon">
              {activity.type.charAt(0)}
            </div>

            <div className="activity-content">

              <p>{activity.message}</p>

              <span>{activity.time}</span>

            </div>

          </div>
        ))}

      </div>

    </div>
  );
}

export default ActivityFeed;