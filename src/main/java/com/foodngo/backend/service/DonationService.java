package com.foodngo.backend.service;

import com.foodngo.backend.dto.DonationRequest;
import com.foodngo.backend.dto.DonationResponse;
import com.foodngo.backend.dto.ImpactRequest;
import com.foodngo.backend.entity.Donation;
import com.foodngo.backend.entity.Driver;
import com.foodngo.backend.entity.Donor;
import com.foodngo.backend.entity.FoodListings;
import com.foodngo.backend.entity.Impact;
import com.foodngo.backend.entity.Ngos;
import com.foodngo.backend.repository.DonationRepository;
import com.foodngo.backend.repository.DonorRepository;
import com.foodngo.backend.repository.DriverRepository;
import com.foodngo.backend.repository.FoodListingRepository;
import com.foodngo.backend.repository.ImpactRepository;
import com.foodngo.backend.repository.NgoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class DonationService {

    private static final Set<String> RESERVED_STATUSES =
            Set.of("ACCEPTED", "DRIVER_ASSIGNED", "PICKED_UP");

    private final DonationRepository donationRepository;
    private final FoodListingRepository foodListingRepository;
    private final NgoRepository ngoRepository;
    private final DonorRepository donorRepository;
    private final DriverRepository driverRepository;
    private final ImpactRepository impactRepository;

    public DonationService(
            DonationRepository donationRepository,
            FoodListingRepository foodListingRepository,
            NgoRepository ngoRepository,
            DonorRepository donorRepository,
            DriverRepository driverRepository,
            ImpactRepository impactRepository) {
        this.donationRepository = donationRepository;
        this.foodListingRepository = foodListingRepository;
        this.ngoRepository = ngoRepository;
        this.donorRepository = donorRepository;
        this.driverRepository = driverRepository;
        this.impactRepository = impactRepository;
    }

    @Transactional
    public DonationResponse createDonation(DonationRequest request) {
        if (request.getFoodListingId() == null || request.getNgoId() == null) {
            throw new IllegalArgumentException("foodListingId and ngoId are required");
        }
        requireNgoOwner(request.getNgoId());
        FoodListings listing = getListingForUpdate(request.getFoodListingId());
        if (!"AVAILABLE".equals(listing.getStatus())) {
            throw new IllegalStateException("Food listing is not available for requests");
        }

        int requestedQuantity = request.getQuantity() == null
                ? listing.getQuantity()
                : request.getQuantity();
        if (requestedQuantity <= 0) {
            throw new IllegalArgumentException("Donation quantity must be positive");
        }
        if (requestedQuantity > availableQuantity(listing)) {
            throw new IllegalStateException("Requested quantity exceeds available food");
        }

        Donation donation = new Donation();
        donation.setFoodListingId(listing.getId());
        donation.setNgoId(request.getNgoId());
        donation.setQuantity(requestedQuantity);
        donation.setStatus("PENDING");
        return convertToResponse(donationRepository.save(donation));
    }

    public List<DonationResponse> getAllDonations() {
        return donationRepository.findAll().stream()
            .filter(this::isVisibleToCurrentUser)
            .map(this::convertToResponse)
            .toList();
    }

    public DonationResponse getDonationById(Long id) {
        Donation donation = getDonation(id);
        if (!isVisibleToCurrentUser(donation)) {
            throw new AccessDeniedException("This donation is not visible to the current account");
        }
        return convertToResponse(donation);
    }

    @Transactional
    public DonationResponse updateDonation(Long id, DonationRequest request) {
        Donation donation = getDonation(id);
        requireNgoOwner(donation.getNgoId());
        requireStatus(donation, "PENDING");
        if (request.getStatus() != null && !"PENDING".equals(request.getStatus())) {
            throw new IllegalStateException("Use the lifecycle actions to change donation status");
        }
        if (request.getFoodListingId() != null
                && !request.getFoodListingId().equals(donation.getFoodListingId())) {
            throw new IllegalArgumentException("A donation cannot be moved to another listing");
        }
        if (request.getNgoId() != null && !request.getNgoId().equals(donation.getNgoId())) {
            throw new IllegalArgumentException("A donation cannot be moved to another NGO");
        }
        if (request.getQuantity() != null) {
            FoodListings listing = getListingForUpdate(donation.getFoodListingId());
            if (request.getQuantity() > availableQuantity(listing)) {
                throw new IllegalStateException("Requested quantity exceeds available food");
            }
            donation.setQuantity(request.getQuantity());
        }
        return convertToResponse(donationRepository.save(donation));
    }

    @Transactional
    public DonationResponse acceptDonation(Long id) {
        Donation donation = getDonation(id);
        requireNgoOwner(donation.getNgoId());
        requireStatus(donation, "PENDING");
        FoodListings listing = getListingForUpdate(donation.getFoodListingId());
        if (!"AVAILABLE".equals(listing.getStatus())
                || donation.getQuantity() > availableQuantity(listing)) {
            throw new IllegalStateException("Requested food is no longer available in that quantity");
        }
        donation.setStatus("ACCEPTED");
        donationRepository.saveAndFlush(donation);
        refreshListingStatus(listing);
        return convertToResponse(donation);
    }

    @Transactional
    public DonationResponse assignDriver(Long id, Long driverId) {
        Donation donation = getDonation(id);
        requireNgoOwner(donation.getNgoId());
        requireStatus(donation, "ACCEPTED");
        if (driverId == null) {
            throw new IllegalArgumentException("driverId is required");
        }
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));
        donation.setDriverId(driver.getId());
        donation.setStatus("DRIVER_ASSIGNED");
        return convertToResponse(donationRepository.save(donation));
    }

    @Transactional
    public DonationResponse pickup(Long id) {
        Donation donation = getDonation(id);
        Driver driver = requireCurrentDriver();
        requireAssignedDriver(donation, driver);
        requireStatus(donation, "DRIVER_ASSIGNED");
        donation.setStatus("PICKED_UP");
        return convertToResponse(donationRepository.save(donation));
    }

    @Transactional
    public DonationResponse deliver(Long id, ImpactRequest request) {
        Donation donation = getDonation(id);
        Driver driver = requireCurrentDriver();
        requireAssignedDriver(donation, driver);
        requireStatus(donation, "PICKED_UP");
        if (request == null || request.getDonationId() == null
                || !id.equals(request.getDonationId())) {
            throw new IllegalArgumentException("Impact donationId must match the donation path");
        }
        if (request.getFoodSaved() == null || request.getFoodSaved() < 0
                || request.getMealsServed() == null || request.getMealsServed() < 0
                || request.getCo2Reduced() == null || request.getCo2Reduced() < 0) {
            throw new IllegalArgumentException("Delivery impact values must be non-negative and present");
        }
        if (impactRepository.existsByDonationId(id)) {
            throw new IllegalStateException("Impact has already been recorded for this donation");
        }

        FoodListings listing = getListingForUpdate(donation.getFoodListingId());
        if (listing.getQuantity() == null || listing.getQuantity() < donation.getQuantity()) {
            throw new IllegalStateException("Listing quantity is insufficient to complete delivery");
        }
        donation.setStatus("DELIVERED");
        donationRepository.saveAndFlush(donation);

        Impact impact = new Impact();
        impact.setDonationId(id);
        impact.setFoodSaved(request.getFoodSaved());
        impact.setMealsServed(request.getMealsServed());
        impact.setCo2Reduced(request.getCo2Reduced());
        impactRepository.save(impact);

        listing.setQuantity(listing.getQuantity() - donation.getQuantity());
        refreshListingStatus(listing);
        foodListingRepository.save(listing);
        return convertToResponse(donation);
    }

    @Transactional
    public void deleteDonation(Long id) {
        Donation donation = getDonation(id);
        requireNgoOwner(donation.getNgoId());
        requireStatus(donation, "PENDING");
        donationRepository.delete(donation);
    }

    private int availableQuantity(FoodListings listing) {
        if (listing.getQuantity() == null || listing.getQuantity() <= 0) {
            return 0;
        }
        int reserved = donationRepository.findByFoodListingIdForUpdate(listing.getId()).stream()
                .filter(donation -> RESERVED_STATUSES.contains(donation.getStatus()))
                .mapToInt(donation -> donation.getQuantity() == null ? 0 : donation.getQuantity())
                .sum();
        return Math.max(0, listing.getQuantity() - reserved);
    }

    private void refreshListingStatus(FoodListings listing) {
        if (listing.getQuantity() == null || listing.getQuantity() <= 0) {
            listing.setStatus("UNAVAILABLE");
        } else if (availableQuantity(listing) <= 0) {
            listing.setStatus("RESERVED");
        } else {
            listing.setStatus("AVAILABLE");
        }
        foodListingRepository.save(listing);
    }

    private FoodListings getListingForUpdate(Long id) {
        return foodListingRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new EntityNotFoundException("Food listing not found"));
    }

    private Donation getDonation(Long id) {
        return donationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Donation not found"));
    }

    private void requireNgoOwner(Long ngoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Ngos ngo = ngoRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new AccessDeniedException("An NGO account is required"));
        if (!ngo.getId().equals(ngoId)) {
            throw new AccessDeniedException("This donation belongs to another NGO");
        }
    }

        private boolean isVisibleToCurrentUser(Donation donation) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isNgo = authentication.getAuthorities().stream()
            .anyMatch(authority -> "ROLE_NGO".equals(authority.getAuthority()));
        if (isNgo) {
            return ngoRepository.findByEmail(authentication.getName())
                .map(ngo -> ngo.getId().equals(donation.getNgoId()))
                .orElse(false);
        }

        boolean isDriver = authentication.getAuthorities().stream()
            .anyMatch(authority -> "ROLE_DRIVER".equals(authority.getAuthority()));
        if (isDriver) {
            return driverRepository.findByEmail(authentication.getName())
                .map(driver -> driver.getId().equals(donation.getDriverId()))
                .orElse(false);
        }

        boolean isDonor = authentication.getAuthorities().stream()
            .anyMatch(authority -> "ROLE_DONOR".equals(authority.getAuthority()));
        if (isDonor) {
            Donor donor = donorRepository.findByEmail(authentication.getName()).orElse(null);
            if (donor == null) {
            return false;
            }
            return foodListingRepository.findById(donation.getFoodListingId())
                .map(listing -> listing.getDonor() != null
                    && donor.getId().equals(listing.getDonor().getId()))
                .orElse(false);
        }
        return false;
        }

    private Driver requireCurrentDriver() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return driverRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new AccessDeniedException("A driver account is required"));
    }

    private void requireAssignedDriver(Donation donation, Driver driver) {
        if (donation.getDriverId() == null || !donation.getDriverId().equals(driver.getId())) {
            throw new AccessDeniedException("This donation is assigned to another driver");
        }
    }

    private void requireStatus(Donation donation, String expected) {
        if (!expected.equals(donation.getStatus())) {
            throw new IllegalStateException(
                    "Donation must be " + expected + " but is " + donation.getStatus());
        }
    }

    private DonationResponse convertToResponse(Donation donation) {
        return new DonationResponse(
                donation.getId(),
                donation.getFoodListingId(),
                donation.getNgoId(),
                donation.getDriverId(),
                donation.getQuantity(),
                donation.getStatus());
    }
}