package com.foodngo.backend.service;

import com.foodngo.backend.dto.FoodPredictionRequest;
import com.foodngo.backend.dto.FoodPredictionResponse;
import com.foodngo.backend.entity.Donor;
import com.foodngo.backend.entity.FoodPrediction;
import com.foodngo.backend.entity.FoodListings;
import com.foodngo.backend.repository.DonationRepository;
import com.foodngo.backend.repository.DonorRepository;
import com.foodngo.backend.repository.FoodPredictionRepository;
import com.foodngo.backend.repository.FoodListingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FoodPredictionService {

    private final FoodPredictionRepository foodPredictionRepository;
    private final FoodListingRepository foodListingRepository;
    private final DonorRepository donorRepository;
    private final DonationRepository donationRepository;

    public FoodPredictionService(
            FoodPredictionRepository foodPredictionRepository,
            FoodListingRepository foodListingRepository,
            DonorRepository donorRepository,
            DonationRepository donationRepository) {

        this.foodPredictionRepository = foodPredictionRepository;
        this.foodListingRepository = foodListingRepository;
        this.donorRepository = donorRepository;
        this.donationRepository = donationRepository;
    }

    @Transactional
    public FoodPredictionResponse createPrediction(FoodPredictionRequest request) {

        FoodListings listing = getListing(request.getFoodListingId());
        requireMutableListing(listing);

        FoodPrediction prediction = new FoodPrediction();

        prediction.setFoodListingId(listing.getId());
        prediction.setFreshness(request.getFreshness());
        prediction.setConfidence(request.getConfidence());

        updateListingStatus(listing, request.getFreshness());

        foodListingRepository.save(listing);

        FoodPrediction savedPrediction =
                foodPredictionRepository.save(prediction);

        return convertToResponse(savedPrediction);
    }

    public List<FoodPredictionResponse> getAllPredictions() {

        return foodPredictionRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public FoodPredictionResponse getPredictionById(Long id) {

        return convertToResponse(getPrediction(id));
    }

    @Transactional
    public FoodPredictionResponse updatePrediction(
            Long id,
            FoodPredictionRequest request) {

        FoodPrediction prediction = getPrediction(id);
        FoodListings listing = getListing(prediction.getFoodListingId());
        requireMutableListing(listing);
        if (request.getFoodListingId() == null
                || !request.getFoodListingId().equals(prediction.getFoodListingId())) {
            throw new IllegalArgumentException("A prediction cannot be moved to another listing");
        }
        prediction.setFreshness(request.getFreshness());
        prediction.setConfidence(request.getConfidence());
        updateListingStatus(listing, request.getFreshness());
        foodListingRepository.save(listing);

        FoodPrediction updatedPrediction =
                foodPredictionRepository.save(prediction);

        return convertToResponse(updatedPrediction);
    }

    @Transactional
    public void deletePrediction(Long id) {
        FoodPrediction prediction = getPrediction(id);
        FoodListings listing = getListing(prediction.getFoodListingId());
        requireMutableListing(listing);
        listing.setStatus("PENDING_PREDICTION");
        foodListingRepository.save(listing);
        foodPredictionRepository.delete(prediction);
    }

    private FoodListings getListing(Long id) {
        return foodListingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Food listing not found"));
    }

    private FoodPrediction getPrediction(Long id) {
        return foodPredictionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Food prediction not found"));
    }

    private void requireMutableListing(FoodListings listing) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Donor donor = donorRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new AccessDeniedException("A donor account is required"));
        if (listing.getDonor() == null || !listing.getDonor().getId().equals(donor.getId())) {
            throw new AccessDeniedException("This listing belongs to another donor");
        }
        if (!"PENDING_PREDICTION".equals(listing.getStatus())
                && !"AVAILABLE".equals(listing.getStatus())
                && !"SPOILED".equals(listing.getStatus())) {
            throw new IllegalStateException("Prediction cannot change after allocation begins");
        }
        if (donationRepository.existsByFoodListingId(listing.getId())) {
            throw new IllegalStateException("A listing cannot be re-predicted after an NGO request");
        }
    }

    private void updateListingStatus(FoodListings listing, String freshness) {
        listing.setStatus("FRESH".equalsIgnoreCase(freshness) ? "AVAILABLE" : "SPOILED");
    }

    private FoodPredictionResponse convertToResponse(
            FoodPrediction prediction) {

        return new FoodPredictionResponse(
                prediction.getId(),
                prediction.getFoodListingId(),
                prediction.getFreshness(),
                prediction.getConfidence()
        );
    }
}