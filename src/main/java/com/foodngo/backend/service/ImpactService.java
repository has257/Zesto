package com.foodngo.backend.service;

import com.foodngo.backend.dto.ImpactRequest;
import com.foodngo.backend.dto.ImpactResponse;
import com.foodngo.backend.entity.Donation;
import com.foodngo.backend.entity.Impact;
import com.foodngo.backend.entity.Ngos;
import com.foodngo.backend.repository.DonationRepository;
import com.foodngo.backend.repository.ImpactRepository;
import com.foodngo.backend.repository.NgoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImpactService {

    private final ImpactRepository impactRepository;
    private final DonationRepository donationRepository;
    private final NgoRepository ngoRepository;

    public ImpactService(
            ImpactRepository impactRepository,
            DonationRepository donationRepository,
            NgoRepository ngoRepository) {
        this.impactRepository = impactRepository;
        this.donationRepository = donationRepository;
        this.ngoRepository = ngoRepository;
    }

    public ImpactResponse createImpact(ImpactRequest request) {
        Donation donation = getDeliveredDonation(request.getDonationId());
        requireNgoOwner(donation);
        if (impactRepository.existsByDonationId(donation.getId())) {
            throw new IllegalStateException("Impact already exists for this donation");
        }

        Impact impact = new Impact();

        impact.setDonationId(request.getDonationId());
        impact.setFoodSaved(request.getFoodSaved());
        impact.setMealsServed(request.getMealsServed());
        impact.setCo2Reduced(request.getCo2Reduced());

        Impact savedImpact = impactRepository.save(impact);

        return convertToResponse(savedImpact);
    }

    public List<ImpactResponse> getAllImpacts() {

        return impactRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public ImpactResponse getImpactById(Long id) {

        Impact impact = getImpact(id);

        return convertToResponse(impact);
    }

    public ImpactResponse updateImpact(
            Long id,
            ImpactRequest request) {

        Impact impact = getImpact(id);
        Donation donation = getDeliveredDonation(impact.getDonationId());
        requireNgoOwner(donation);
        if (!impact.getDonationId().equals(request.getDonationId())) {
            throw new IllegalArgumentException("An impact record cannot be moved to another donation");
        }

        impact.setDonationId(request.getDonationId());
        impact.setFoodSaved(request.getFoodSaved());
        impact.setMealsServed(request.getMealsServed());
        impact.setCo2Reduced(request.getCo2Reduced());

        Impact updatedImpact = impactRepository.save(impact);

        return convertToResponse(updatedImpact);
    }

    public void deleteImpact(Long id) {
        Impact impact = getImpact(id);
        requireNgoOwner(getDeliveredDonation(impact.getDonationId()));
        throw new IllegalStateException("Impact records are retained after successful delivery");
    }

    private Impact getImpact(Long id) {
        return impactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Impact record not found"));
    }

    private Donation getDeliveredDonation(Long donationId) {
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new EntityNotFoundException("Donation not found"));
        if (!"DELIVERED".equals(donation.getStatus())) {
            throw new IllegalStateException("Impact can be recorded only after delivery");
        }
        return donation;
    }

    private void requireNgoOwner(Donation donation) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Ngos ngo = ngoRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new AccessDeniedException("An NGO account is required"));
        if (!ngo.getId().equals(donation.getNgoId())) {
            throw new AccessDeniedException("This impact belongs to another NGO");
        }
    }

    private ImpactResponse convertToResponse(Impact impact) {

        return new ImpactResponse(
                impact.getId(),
                impact.getDonationId(),
                impact.getFoodSaved(),
                impact.getMealsServed(),
                impact.getCo2Reduced()
        );
    }
}