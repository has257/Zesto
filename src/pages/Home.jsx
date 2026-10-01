import Navbar from "../components/Navbar";

function Home() {
  return (
    <div>

      <Navbar />

      <main>
        <h1>Welcome to FoodRescue</h1>

        <p>
          Connecting surplus food from donors with NGOs
          that can distribute it to people in need.
        </p>
      </main>

    </div>
  );
}

export default Home;