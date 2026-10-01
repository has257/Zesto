package com.foodngo.backend;

import com.foodngo.backend.dto.DonationRequest;
import com.foodngo.backend.dto.DonationResponse;
import com.foodngo.backend.dto.ImpactRequest;
import com.foodngo.backend.entity.Donor;
import com.foodngo.backend.entity.Driver;
import com.foodngo.backend.entity.FoodListings;
import com.foodngo.backend.entity.Ngos;
import com.foodngo.backend.repository.DonorRepository;
import com.foodngo.backend.repository.DriverRepository;
import com.foodngo.backend.repository.FoodListingRepository;
import com.foodngo.backend.repository.ImpactRepository;
import com.foodngo.backend.repository.NgoRepository;
import com.foodngo.backend.service.DonationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BackendApplicationTests {

	@Autowired
	private DonationService donationService;

	@Autowired
	private DonorRepository donorRepository;

	@Autowired
	private NgoRepository ngoRepository;

	@Autowired
	private DriverRepository driverRepository;

	@Autowired
	private FoodListingRepository foodListingRepository;

	@Autowired
	private ImpactRepository impactRepository;

	@Test
	void contextLoads() {
	}

	@Test
	@Transactional
	void deliveryRecordsImpactAndConsumesTheListingQuantity() {
		String suffix = UUID.randomUUID().toString();
		Donor donor = new Donor("Test Donor", "donor-" + suffix + "@example.invalid",
				"unused-password", "5550101", "Test address");
		donor = donorRepository.save(donor);

		Ngos ngo = new Ngos();
		ngo.setName("Test NGO");
		ngo.setEmail("ngo-" + suffix + "@example.invalid");
		ngo.setPassword("unused-password");
		ngo = ngoRepository.save(ngo);

		Driver driver = new Driver();
		driver.setName("Test Driver");
		driver.setEmail("driver-" + suffix + "@example.invalid");
		driver.setPassword("unused-password");
		driver = driverRepository.save(driver);

		FoodListings listing = new FoodListings();
		listing.setName("Test food");
		listing.setQuantity(8);
		listing.setExpiryDate("2099-01-01");
		listing.setStatus("AVAILABLE");
		listing.setDonor(donor);
		listing = foodListingRepository.save(listing);

		try {
			setAuthenticatedUser(ngo.getEmail(), "NGO");
			DonationRequest request = new DonationRequest();
			request.setFoodListingId(listing.getId());
			request.setNgoId(ngo.getId());
			request.setQuantity(8);
			DonationResponse pending = donationService.createDonation(request);
			assertEquals("PENDING", pending.getStatus());

			DonationResponse accepted = donationService.acceptDonation(pending.getId());
			assertEquals("ACCEPTED", accepted.getStatus());
			DonationResponse assigned = donationService.assignDriver(pending.getId(), driver.getId());
			assertEquals("DRIVER_ASSIGNED", assigned.getStatus());

			setAuthenticatedUser(driver.getEmail(), "DRIVER");
			DonationResponse pickedUp = donationService.pickup(pending.getId());
			assertEquals("PICKED_UP", pickedUp.getStatus());

			ImpactRequest impact = new ImpactRequest();
			impact.setDonationId(pending.getId());
			impact.setFoodSaved(8.0);
			impact.setMealsServed(8);
			impact.setCo2Reduced(2.0);
			DonationResponse delivered = donationService.deliver(pending.getId(), impact);
			assertEquals("DELIVERED", delivered.getStatus());
			assertTrue(impactRepository.existsByDonationId(pending.getId()));

			FoodListings depletedListing = foodListingRepository.findById(listing.getId()).orElseThrow();
			assertEquals(0, depletedListing.getQuantity());
			assertEquals("UNAVAILABLE", depletedListing.getStatus());
			assertThrows(IllegalStateException.class, () -> donationService.pickup(pending.getId()));
		} finally {
			SecurityContextHolder.clearContext();
		}
	}

	private void setAuthenticatedUser(String email, String role) {
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(email, null,
						List.of(new SimpleGrantedAuthority("ROLE_" + role))));
	}

}
