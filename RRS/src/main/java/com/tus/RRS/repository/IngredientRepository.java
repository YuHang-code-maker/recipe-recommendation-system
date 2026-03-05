package com.tus.RRS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tus.RRS.entity.IngredientEntity;

@Repository
public interface IngredientRepository extends JpaRepository<IngredientEntity,Long> {

}
