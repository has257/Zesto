package com.foodngo.backend.controller;

import com.foodngo.backend.dto.DonationRequest;
import com.foodngo.backend.dto.DonationResponse;
import com.foodngo.backend.dto.ImpactRequest;
import com.foodngo.backend.service.DonationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donations")
public class DonationController {

    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

        @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public DonationResponse createDonation(
            @Valid @RequestBody DonationRequest request) {

        return donationService.createDonation(request);
    }

    @GetMapping
    public List<DonationResponse> getAllDonations() {

        return donationService.getAllDonations();
    }

    @GetMapping("/{id}")
    public DonationResponse getDonationById(
            @PathVariable Long id) {

        return donationService.getDonationById(id);
    }

    @PutMapping("/{id}")
    public DonationResponse updateDonation(
            @PathVariable Long id,
            @Valid @RequestBody DonationRequest request) {

        return donationService.updateDonation(id, request);
    }

    @PostMapping("/{id}/accept")
    public DonationResponse acceptDonation(@PathVariable Long id) {
        return donationService.acceptDonation(id);
    }

    @PostMapping("/{id}/assign-driver")
    public DonationResponse assignDriver(
            @PathVariable Long id,
            @Valid @RequestBody DonationRequest request) {
        return donationService.assignDriver(id, request.getDriverId());
    }

    @PostMapping("/{id}/pickup")
    public DonationResponse pickup(@PathVariable Long id) {
        return donationService.pickup(id);
    }

    @PostMapping("/{id}/deliver")
    public DonationResponse deliver(
            @PathVariable Long id,
            @Valid @RequestBody ImpactRequest request) {
        return donationService.deliver(id, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteDonation(@PathVariable Long id) {

        donationService.deleteDonation(id);
    }
}