package com.foodngo.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class ImpactRequest {

    @NotNull
    private Long donationId;
    @NotNull
    @DecimalMin("0.0")
    private Double foodSaved;
    @NotNull
    @PositiveOrZero
    private Integer mealsServed;
    @NotNull
    @DecimalMin("0.0")
    private Double co2Reduced;

    public ImpactRequest() {
    }

    public Long getDonationId() {
        return donationId;
    }

    public void setDonationId(Long donationId) {
        this.donationId = donationId;
    }

    public Double getFoodSaved() {
        return foodSaved;
    }

    public void setFoodSaved(Double foodSaved) {
        this.foodSaved = foodSaved;
    }

    public Integer getMealsServed() {
        return mealsServed;
    }

    public void setMealsServed(Integer mealsServed) {
        this.mealsServed = mealsServed;
    }

    public Double getCo2Reduced() {
        return co2Reduced;
    }

    public void setCo2Reduced(Double co2Reduced) {
        this.co2Reduced = co2Reduced;
    }
}