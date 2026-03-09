package com.tus.RRS.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import com.tus.RRS.dto.IngredientDto;
import com.tus.RRS.dto.RecipeDto;
import com.tus.RRS.entity.IngredientEntity;
import com.tus.RRS.entity.RecipeEntity;

public class RecipeMapper {
	public static RecipeDto mapToRecipeDto(RecipeEntity recipe, RecipeDto recipeDto) {
		recipeDto.setId(recipe.getId());
		recipeDto.setTitle(recipe.getTitle());
		recipeDto.setInstructions(recipe.getInstructions());
		recipeDto.setImage(recipe.getImage());
		Set<IngredientDto> ingredientDto = recipe.getIngredients().stream()
				.map(ingredient ->
	               new IngredientDto(
	                   ingredient.getId(),
	                   ingredient.getName()
	               ))
	          .collect(Collectors.toSet());
		recipeDto.setIngredients(ingredientDto);
		return recipeDto;
	}
	
	public static RecipeEntity mapToRecipeEntity(RecipeDto recipeDto, RecipeEntity recipe) {
		recipe.setId(recipeDto.getId());
		recipe.setTitle(recipeDto.getTitle());
		recipe.setImage(recipeDto.getImage());
		recipe.setInstructions(recipeDto.getInstructions());
		Set<IngredientEntity> ingredients= recipeDto.getIngredients().stream()
											.map(i->{
											    IngredientEntity ingredient = new IngredientEntity();
											    ingredient.setId(i.getId());
											    ingredient.setName(i.getName());
											    return ingredient;
											}).collect(Collectors.toSet());
		recipe.setIngredients(ingredients);
		return recipe;
	}
}
