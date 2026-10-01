package com.foodngo.backend.repository;

import com.foodngo.backend.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonorRepository extends JpaRepository<Donor, Long> {
	java.util.Optional<Donor> findByEmail(String email);

	boolean existsByEmail(String email);
}