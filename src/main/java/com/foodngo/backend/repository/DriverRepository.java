package com.foodngo.backend.repository;

import com.foodngo.backend.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
	java.util.Optional<Driver> findByEmail(String email);

	boolean existsByEmail(String email);
}