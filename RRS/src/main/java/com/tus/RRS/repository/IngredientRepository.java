package com.tus.RRS.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tus.RRS.entity.IngredientEntity;

@Repository
public interface IngredientRepository extends JpaRepository<IngredientEntity,Long> {
	Optional<IngredientEntity> findByName(String name);
}
