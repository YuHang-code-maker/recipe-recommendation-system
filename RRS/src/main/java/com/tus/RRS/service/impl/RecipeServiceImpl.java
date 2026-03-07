package com.tus.RRS.service.impl;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tus.RRS.dto.RecipeDto;
import com.tus.RRS.entity.IngredientEntity;
import com.tus.RRS.entity.RecipeEntity;
import com.tus.RRS.mapper.RecipeMapper;
import com.tus.RRS.repository.IngredientRepository;
import com.tus.RRS.repository.RecipeRepository;
import com.tus.RRS.service.IRecipeService;

@Service
public class RecipeServiceImpl implements IRecipeService{
	private RecipeRepository recipeRepository;
	private IngredientRepository ingredientRepository;
	public RecipeServiceImpl(RecipeRepository recipeRepository,IngredientRepository ingredientRepository) {
		this.recipeRepository = recipeRepository;
		this.ingredientRepository = ingredientRepository;
	}
	@Override
	public void createRecipe(RecipeDto recipeDto) {
		RecipeEntity recipe = RecipeMapper.mapToRecipeDtoEntity(recipeDto, new RecipeEntity());
		Set<IngredientEntity> ingredients= recipeDto.getIngredients().stream()
				.map(i->{
				    IngredientEntity ingredient = new IngredientEntity();
				    ingredient.setId(i.getId());
				    ingredient.setName(i.getName());
				    return ingredient;
				}).collect(Collectors.toSet());
		recipe.setIngredients(ingredients);
		recipeRepository.save(recipe);
	}

}
