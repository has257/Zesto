package com.foodngo.backend.repository;

import com.foodngo.backend.entity.FoodListings;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FoodListingRepository extends JpaRepository<FoodListings, Long> {
		java.util.List<FoodListings> findByStatus(String status);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select listing from FoodListings listing where listing.id = :id")
	Optional<FoodListings> findByIdForUpdate(@Param("id") Long id);
}