import "./UserManagement.css";

function UserManagement() {
  const users = [
    {
      id: 1,
      name: "Green Leaf Restaurant",
      email: "greenleaf@example.com",
      role: "Donor",
      status: "Active",
    },
    {
      id: 2,
      name: "Fresh Bakes",
      email: "freshbakes@example.com",
      role: "Donor",
      status: "Active",
    },
    {
      id: 3,
      name: "Helping Hands NGO",
      email: "helpinghands@example.com",
      role: "NGO",
      status: "Active",
    },
    {
      id: 4,
      name: "Hope Foundation",
      email: "hopefoundation@example.com",
      role: "NGO",
      status: "Pending",
    },
  ];

  return (
    <div className="user-management">

      <div className="user-management-header">
        <h2>User Management</h2>

        <p>
          Manage registered donors and NGOs on the platform.
        </p>
      </div>

      <div className="users-table">

        <div className="users-table-header">
          <span>Name</span>
          <span>Email</span>
          <span>Role</span>
          <span>Status</span>
        </div>

        {users.map((user) => (
          <div
            className="users-table-row"
            key={user.id}
          >
            <span>{user.name}</span>

            <span>{user.email}</span>

            <span>
              <span
                className={`user-role user-role-${user.role.toLowerCase()}`}
              >
                {user.role}
              </span>
            </span>

            <span>
              <span
                className={`user-status user-status-${user.status.toLowerCase()}`}
              >
                {user.status}
              </span>
            </span>
          </div>
        ))}

      </div>

    </div>
  );
}

export default UserManagement;