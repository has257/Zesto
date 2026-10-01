import { useState } from "react";
import "./DonationForm.css";

function DonationForm() {
  const [foodType, setFoodType] = useState("");
  const [quantity, setQuantity] = useState("");
  const [expiryTime, setExpiryTime] = useState("");
  const [description, setDescription] = useState("");
  const [submitted, setSubmitted] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();

    console.log({
      foodType,
      quantity,
      expiryTime,
      description,
    });

    setSubmitted(true);

    setFoodType("");
    setQuantity("");
    setExpiryTime("");
    setDescription("");
  };

  return (
    <div className="donation-form-card">

      <div className="donation-form-header">
        <h2>Add Food Donation</h2>

        <p>
          Provide details about the food you want to donate.
        </p>
      </div>

      {submitted && (
        <div className="donation-success-message">
          Donation created successfully.
        </div>
      )}

      <form onSubmit={handleSubmit}>

        <div className="form-row">

          <div className="form-group">
            <label>Food Type</label>

            <select
              value={foodType}
              onChange={(e) => setFoodType(e.target.value)}
              required
            >
              <option value="">Select food type</option>
              <option value="cooked-food">Cooked Food</option>
              <option value="packaged-food">Packaged Food</option>
              <option value="fruits-vegetables">
                Fruits & Vegetables
              </option>
              <option value="bakery">Bakery Items</option>
              <option value="other">Other</option>
            </select>
          </div>

          <div className="form-group">
            <label>Quantity</label>

            <input
              type="number"
              min="1"
              placeholder="Quantity in kg"
              value={quantity}
              onChange={(e) => setQuantity(e.target.value)}
              required
            />
          </div>

        </div>

        <div className="form-group">
          <label>Best Before</label>

          <input
            type="datetime-local"
            value={expiryTime}
            onChange={(e) => setExpiryTime(e.target.value)}
            required
          />
        </div>

        <div className="form-group">
          <label>Description</label>

          <textarea
            rows="4"
            placeholder="Describe the food, packaging, pickup instructions, etc."
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
        </div>

        <button
          type="submit"
          className="donation-submit-button"
        >
          Create Donation
        </button>

      </form>

    </div>
  );
}

export default DonationForm;