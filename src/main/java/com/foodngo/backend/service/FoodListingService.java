package com.foodngo.backend.service;

import com.foodngo.backend.dto.FoodListingRequest;
import com.foodngo.backend.dto.FoodListingResponse;
import com.foodngo.backend.entity.Donor;
import com.foodngo.backend.entity.FoodListings;
import com.foodngo.backend.repository.DonorRepository;
import com.foodngo.backend.repository.DonationRepository;
import com.foodngo.backend.repository.FoodListingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodListingService {

    private final FoodListingRepository foodListingRepository;
    private final DonorRepository donorRepository;
    private final DonationRepository donationRepository;

    public FoodListingService(
            FoodListingRepository foodListingRepository,
            DonorRepository donorRepository,
            DonationRepository donationRepository) {

        this.foodListingRepository = foodListingRepository;
        this.donorRepository = donorRepository;
        this.donationRepository = donationRepository;
    }

    public FoodListingResponse createListing(FoodListingRequest request) {

        Donor donor = currentDonor();
        if (request.getDonorId() == null || !request.getDonorId().equals(donor.getId())) {
            throw new AccessDeniedException("A donor can create listings only for their own account");
        }
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Listing quantity must be positive");
        }
        FoodListings listing = new FoodListings();

        listing.setName(request.getName());
        listing.setDescription(request.getDescription());
        listing.setQuantity(request.getQuantity());
        listing.setExpiryDate(request.getExpiryDate());
        listing.setStatus("PENDING_PREDICTION");
        listing.setDonor(donor);

        FoodListings savedListing = foodListingRepository.save(listing);

        return convertToResponse(savedListing);
    }

    public List<FoodListingResponse> getAllListings() {

        return foodListingRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public List<FoodListingResponse> getAvailableListings() {
        return foodListingRepository.findByStatus("AVAILABLE")
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public FoodListingResponse getListingById(Long id) {

        return convertToResponse(getListing(id));
    }

    public FoodListingResponse updateListing(
            Long id,
            FoodListingRequest request) {

        FoodListings listing = getListing(id);
        requireOwner(listing);
        if (donationRepository.existsByFoodListingId(id)) {
            throw new IllegalStateException("A listing cannot be edited after an NGO has requested it");
        }
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Listing quantity must be positive");
        }

        listing.setName(request.getName());
        listing.setDescription(request.getDescription());
        listing.setQuantity(request.getQuantity());
        listing.setExpiryDate(request.getExpiryDate());
        if (request.getDonorId() != null
                && !request.getDonorId().equals(listing.getDonor().getId())) {
            throw new IllegalArgumentException("A listing cannot be moved to another donor");
        }

        FoodListings updatedListing =
                foodListingRepository.save(listing);

        return convertToResponse(updatedListing);
    }

    public void deleteListing(Long id) {
        FoodListings listing = getListing(id);
        requireOwner(listing);
        if (donationRepository.existsByFoodListingId(id)) {
            throw new IllegalStateException("A listing with donation requests cannot be deleted");
        }
        foodListingRepository.delete(listing);
    }

    private FoodListings getListing(Long id) {
        return foodListingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Food listing not found"));
    }

    private Donor currentDonor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return donorRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new AccessDeniedException("A donor account is required"));
    }

    private void requireOwner(FoodListings listing) {
        if (listing.getDonor() == null || !listing.getDonor().getId().equals(currentDonor().getId())) {
            throw new AccessDeniedException("This listing belongs to another donor");
        }
    }

    private FoodListingResponse convertToResponse(
            FoodListings listing) {

        return new FoodListingResponse(
                listing.getId(),
                listing.getName(),
                listing.getDescription(),
                listing.getQuantity(),
                listing.getExpiryDate(),
                listing.getStatus()
        );
    }
}