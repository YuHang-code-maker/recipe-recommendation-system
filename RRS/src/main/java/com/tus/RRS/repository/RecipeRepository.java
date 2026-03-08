package com.tus.RRS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tus.RRS.entity.RecipeEntity;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity,Long>{
	boolean existsByTitle(String title);
}
