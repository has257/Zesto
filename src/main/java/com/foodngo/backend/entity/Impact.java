package com.foodngo.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Impact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long donationId;
    private Double foodSaved;
    private Integer mealsServed;
    private Double co2Reduced;

    public Impact() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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