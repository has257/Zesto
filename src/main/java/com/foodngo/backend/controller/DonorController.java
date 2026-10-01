package com.foodngo.backend.controller;

import com.foodngo.backend.dto.DonorRequest;
import com.foodngo.backend.dto.DonorResponse;
import com.foodngo.backend.service.DonorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donors")
public class DonorController {

    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }

        @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public DonorResponse createDonor(
            @Valid @RequestBody DonorRequest request) {

        return donorService.createDonor(request);
    }

    @GetMapping
    public List<DonorResponse> getAllDonors() {
        return donorService.getAllDonors();
    }

    @GetMapping("/{id}")
    public DonorResponse getDonorById(
            @PathVariable Long id) {

        return donorService.getDonorById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteDonor(
            @PathVariable Long id) {

        donorService.deleteDonor(id);

        return "Donor deleted successfully";
    }
}