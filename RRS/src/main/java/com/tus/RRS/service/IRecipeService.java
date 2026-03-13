package com.tus.RRS.service;

import java.util.List;

import com.tus.RRS.dto.RecipeDto;

public interface IRecipeService {
	void createRecipe(RecipeDto recipeDto);
	RecipeDto fetchRecipe(String title);
	boolean updateRecipe(Long id,RecipeDto recipeDto);
	boolean deleteRecipe(Long id);
	List<RecipeDto> getAllRecipes();
}
