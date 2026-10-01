package com.foodngo.backend.dto;

public class DonationResponse {

    private Long id;
    private Long foodListingId;
    private Long ngoId;
    private Long driverId;
    private Integer quantity;
    private String status;

    public DonationResponse(
            Long id,
            Long foodListingId,
            Long ngoId,
            Long driverId,
            Integer quantity,
            String status) {

        this.id = id;
        this.foodListingId = foodListingId;
        this.ngoId = ngoId;
        this.driverId = driverId;
        this.quantity = quantity;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getFoodListingId() {
        return foodListingId;
    }

    public Long getNgoId() {
        return ngoId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }
}