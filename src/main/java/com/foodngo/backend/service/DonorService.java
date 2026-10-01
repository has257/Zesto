package com.foodngo.backend.service;

import com.foodngo.backend.dto.DonorRequest;
import com.foodngo.backend.dto.DonorResponse;
import com.foodngo.backend.entity.Donor;
import com.foodngo.backend.repository.DonorRepository;
import com.foodngo.backend.repository.DriverRepository;
import com.foodngo.backend.repository.NgoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonorService {

    private final DonorRepository donorRepository;
    private final NgoRepository ngoRepository;
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;

    public DonorService(
            DonorRepository donorRepository,
            NgoRepository ngoRepository,
            DriverRepository driverRepository,
            PasswordEncoder passwordEncoder) {
        this.donorRepository = donorRepository;
        this.ngoRepository = ngoRepository;
        this.driverRepository = driverRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public DonorResponse createDonor(DonorRequest request) {
        if (donorRepository.existsByEmail(request.getEmail())
                || ngoRepository.existsByEmail(request.getEmail())
                || driverRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("An account already uses this email");
        }

        Donor donor = new Donor();

        donor.setName(request.getName());
        donor.setEmail(request.getEmail());
        donor.setPassword(passwordEncoder.encode(request.getPassword()));
        donor.setPhone(request.getPhone());
        donor.setAddress(request.getAddress());

        Donor savedDonor = donorRepository.save(donor);

        return convertToResponse(savedDonor);
    }

    public List<DonorResponse> getAllDonors() {
        return donorRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public DonorResponse getDonorById(Long id) {

        Donor donor = donorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Donor not found"));

        return convertToResponse(donor);
    }

    public void deleteDonor(Long id) {
        Donor donor = donorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Donor not found"));
        if (!donor.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new AccessDeniedException("A donor can delete only their own account");
        }
        donorRepository.delete(donor);
    }

    private DonorResponse convertToResponse(Donor donor) {

        return new DonorResponse(
                donor.getId(),
                donor.getName(),
                donor.getEmail(),
                donor.getPhone(),
                donor.getAddress()
        );
    }
}