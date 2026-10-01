package com.foodngo.backend.repository;

import com.foodngo.backend.entity.Impact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImpactRepository extends JpaRepository<Impact, Long> {
	boolean existsByDonationId(Long donationId);
}