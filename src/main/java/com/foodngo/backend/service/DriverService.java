package com.foodngo.backend.service;

import com.foodngo.backend.entity.Driver;
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
public class DriverService {

    private final DriverRepository repository;
    private final DonorRepository donorRepository;
    private final NgoRepository ngoRepository;
    private final PasswordEncoder passwordEncoder;

    public DriverService(
            DriverRepository repository,
            DonorRepository donorRepository,
            NgoRepository ngoRepository,
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.donorRepository = donorRepository;
        this.ngoRepository = ngoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Driver createDriver(Driver driver) {
        if (repository.existsByEmail(driver.getEmail())
                || donorRepository.existsByEmail(driver.getEmail())
                || ngoRepository.existsByEmail(driver.getEmail())) {
            throw new IllegalStateException("An account already uses this email");
        }
        driver.setPassword(passwordEncoder.encode(driver.getPassword()));
        return repository.save(driver);
    }

    public List<Driver> getAllDrivers() {
        return repository.findAll();
    }

    public Driver getDriverById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Driver not found"));
    }

    public void deleteDriver(Long id) {
        Driver driver = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Driver not found"));
        if (!driver.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            throw new AccessDeniedException("A driver can delete only their own account");
        }
        repository.delete(driver);
    }
}