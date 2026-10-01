package com.foodngo.backend.repository;

import com.foodngo.backend.entity.Donation;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {
	List<Donation> findByFoodListingId(Long foodListingId);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select donation from Donation donation where donation.foodListingId = :foodListingId")
	List<Donation> findByFoodListingIdForUpdate(@Param("foodListingId") Long foodListingId);

	boolean existsByFoodListingId(Long foodListingId);
}