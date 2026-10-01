package com.foodngo.backend.dto;

public class FoodListingResponse {

    private Long id;
    private String name;
    private String description;
    private Integer quantity;
    private String expiryDate;
    private String status;

    public FoodListingResponse() {
    }

    public FoodListingResponse(
            Long id,
            String name,
            String description,
            Integer quantity,
            String expiryDate,
            String status) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}