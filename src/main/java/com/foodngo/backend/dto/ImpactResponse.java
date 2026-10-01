package com.foodngo.backend.dto;

public class ImpactResponse {

    private Long id;
    private Long donationId;
    private Double foodSaved;
    private Integer mealsServed;
    private Double co2Reduced;

    public ImpactResponse(
            Long id,
            Long donationId,
            Double foodSaved,
            Integer mealsServed,
            Double co2Reduced) {

        this.id = id;
        this.donationId = donationId;
        this.foodSaved = foodSaved;
        this.mealsServed = mealsServed;
        this.co2Reduced = co2Reduced;
    }

    public Long getId() {
        return id;
    }

    public Long getDonationId() {
        return donationId;
    }

    public Double getFoodSaved() {
        return foodSaved;
    }

    public Integer getMealsServed() {
        return mealsServed;
    }

    public Double getCo2Reduced() {
        return co2Reduced;
    }
}