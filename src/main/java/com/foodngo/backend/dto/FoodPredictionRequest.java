package com.foodngo.backend.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class FoodPredictionRequest {

    @NotNull
    private Long foodListingId;
    @NotBlank
    @Pattern(regexp = "FRESH|SPOILED", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String freshness;
    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("1.0")
    private Double confidence;

    public FoodPredictionRequest() {
    }

    public Long getFoodListingId() {
        return foodListingId;
    }

    public void setFoodListingId(Long foodListingId) {
        this.foodListingId = foodListingId;
    }

    public String getFreshness() {
        return freshness;
    }

    public void setFreshness(String freshness) {
        this.freshness = freshness;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
}