package com.foodngo.backend.controller;

import com.foodngo.backend.dto.FoodPredictionRequest;
import com.foodngo.backend.dto.FoodPredictionResponse;
import com.foodngo.backend.service.FoodPredictionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food-predictions")
public class FoodPredictionController {

    private final FoodPredictionService foodPredictionService;

    public FoodPredictionController(
            FoodPredictionService foodPredictionService) {
        this.foodPredictionService = foodPredictionService;
    }

        @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public FoodPredictionResponse createPrediction(
            @Valid @RequestBody FoodPredictionRequest request) {

        return foodPredictionService.createPrediction(request);
    }

    @GetMapping
    public List<FoodPredictionResponse> getAllPredictions() {

        return foodPredictionService.getAllPredictions();
    }

    @GetMapping("/{id}")
    public FoodPredictionResponse getPredictionById(
            @PathVariable Long id) {

        return foodPredictionService.getPredictionById(id);
    }

    @PutMapping("/{id}")
    public FoodPredictionResponse updatePrediction(
            @PathVariable Long id,
            @Valid @RequestBody FoodPredictionRequest request) {

        return foodPredictionService.updatePrediction(id, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deletePrediction(@PathVariable Long id) {

        foodPredictionService.deletePrediction(id);
    }
}