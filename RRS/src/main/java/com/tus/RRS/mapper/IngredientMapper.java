package com.tus.RRS.mapper;

import com.tus.RRS.dto.IngredientDto;
import com.tus.RRS.entity.IngredientEntity;

public class IngredientMapper {
	public static IngredientDto mapToIngredientDto(IngredientEntity ingredient, IngredientDto ingredientDto) {
		ingredientDto.setId(ingredient.getId());
		ingredientDto.setName(ingredient.getName());
		return ingredientDto;
	}
	
	public static IngredientEntity mapToIngredientEntity(IngredientDto ingredientDto,IngredientEntity ingredient) {
		ingredient.setId(ingredientDto.getId());
		ingredient.setName(ingredientDto.getName());
		return ingredient;
	}
}
