package com.foodngo.backend.controller;

import com.foodngo.backend.dto.FoodListingRequest;
import com.foodngo.backend.dto.FoodListingResponse;
import com.foodngo.backend.service.FoodListingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food-listings")
public class FoodListingController {

    private final FoodListingService foodListingService;

    public FoodListingController(FoodListingService foodListingService) {
        this.foodListingService = foodListingService;
    }

        @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public FoodListingResponse createListing(
            @Valid @RequestBody FoodListingRequest request) {

        return foodListingService.createListing(request);
    }

    @GetMapping
    public List<FoodListingResponse> getAllListings() {

        return foodListingService.getAllListings();
    }

    @GetMapping("/available")
    public List<FoodListingResponse> getAvailableListings() {
        return foodListingService.getAvailableListings();
    }

    @GetMapping("/{id}")
    public FoodListingResponse getListingById(
            @PathVariable Long id) {

        return foodListingService.getListingById(id);
    }

    @PutMapping("/{id}")
    public FoodListingResponse updateListing(
            @PathVariable Long id,
            @Valid @RequestBody FoodListingRequest request) {

        return foodListingService.updateListing(id, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteListing(@PathVariable Long id) {

        foodListingService.deleteListing(id);
    }
}