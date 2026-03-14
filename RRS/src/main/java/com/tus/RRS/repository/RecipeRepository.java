package com.tus.RRS.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tus.RRS.entity.RecipeEntity;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity,Long>{
	boolean existsByTitle(String title);
	
	Optional<RecipeEntity> findByTitle(String title);
	
	List<RecipeEntity> findByTitleContainingIgnoreCase(String title);
}
