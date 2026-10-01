package com.foodngo.backend.dto;

public class FoodPredictionResponse {

    private Long id;
    private Long foodListingId;
    private String freshness;
    private Double confidence;

    public FoodPredictionResponse(
            Long id,
            Long foodListingId,
            String freshness,
            Double confidence) {

        this.id = id;
        this.foodListingId = foodListingId;
        this.freshness = freshness;
        this.confidence = confidence;
    }

    public Long getId() {
        return id;
    }

    public Long getFoodListingId() {
        return foodListingId;
    }

    public String getFreshness() {
        return freshness;
    }

    public Double getConfidence() {
        return confidence;
    }
}