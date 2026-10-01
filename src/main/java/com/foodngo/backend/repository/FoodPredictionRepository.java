package com.foodngo.backend.repository;

import com.foodngo.backend.entity.FoodPrediction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodPredictionRepository extends JpaRepository<FoodPrediction, Long> {
}