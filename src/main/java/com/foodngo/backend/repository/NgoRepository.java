package com.foodngo.backend.repository;

import com.foodngo.backend.entity.Ngos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NgoRepository extends JpaRepository<Ngos, Long> {
	java.util.Optional<Ngos> findByEmail(String email);

	boolean existsByEmail(String email);
}