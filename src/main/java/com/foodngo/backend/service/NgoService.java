package com.foodngo.backend.service;

import com.foodngo.backend.dto.NgoRequest;
import com.foodngo.backend.dto.NgoResponse;
import com.foodngo.backend.entity.Ngos;
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
public class NgoService {

    private final NgoRepository ngoRepository;
    private final DonorRepository donorRepository;
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;

    public NgoService(
            NgoRepository ngoRepository,
            DonorRepository donorRepository,
            DriverRepository driverRepository,
            PasswordEncoder passwordEncoder) {
        this.ngoRepository = ngoRepository;
        this.donorRepository = donorRepository;
        this.driverRepository = driverRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public NgoResponse createNgo(NgoRequest request) {
        if (ngoRepository.existsByEmail(request.getEmail())
                || donorRepository.existsByEmail(request.getEmail())
                || driverRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("An account already uses this email");
        }

        Ngos ngo = new Ngos();

        ngo.setName(request.getName());
        ngo.setEmail(request.getEmail());
        ngo.setPassword(passwordEncoder.encode(request.getPassword()));
        ngo.setPhone(request.getPhone());
        ngo.setAddress(request.getAddress());

        Ngos savedNgo = ngoRepository.save(ngo);

        return convertToResponse(savedNgo);
    }

    public List<NgoResponse> getAllNgos() {

        return ngoRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public NgoResponse getNgoById(Long id) {

        Ngos ngo = ngoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("NGO not found"));

        return convertToResponse(ngo);
    }

    public void deleteNgo(Long id) {
        Ngos ngo = ngoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("NGO not found"));
        if (!ngo.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new AccessDeniedException("An NGO can delete only its own account");
        }
        ngoRepository.delete(ngo);
    }

    private NgoResponse convertToResponse(Ngos ngo) {

        return new NgoResponse(
                ngo.getId(),
                ngo.getName(),
                ngo.getEmail(),
                ngo.getPhone(),
                ngo.getAddress()
        );
    }
}