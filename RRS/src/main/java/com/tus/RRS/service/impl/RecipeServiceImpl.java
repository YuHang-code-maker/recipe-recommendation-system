package com.tus.RRS.service.impl;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tus.RRS.dto.RecipeDto;
import com.tus.RRS.entity.IngredientEntity;
import com.tus.RRS.entity.RecipeEntity;
import com.tus.RRS.exception.DuplicateResourceException;
import com.tus.RRS.exception.ResourceNotFoundException;
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
		RecipeEntity recipe = RecipeMapper.mapToRecipeEntity(recipeDto, new RecipeEntity());
		if(recipeRepository.existsByTitle(recipeDto.getTitle())){
			throw new DuplicateResourceException("Recipe already existed "+recipeDto.getTitle());
		}
		Set<IngredientEntity> ingredients= recipeDto.getIngredients().stream()
				.map(i->{
					Optional<IngredientEntity> optionalIngredient = ingredientRepository.findByName(i.getName());
					if(optionalIngredient.isPresent()) {
						return optionalIngredient.get();
					}
				    IngredientEntity ingredient = new IngredientEntity();
				    ingredient.setName(i.getName());
				    return ingredientRepository.save(ingredient);
				}).collect(Collectors.toSet());
		
		recipe.setIngredients(ingredients);
		recipeRepository.save(recipe);
	}
	@Override
	public RecipeDto fetchRecipe(String title) {
		RecipeEntity recipe = recipeRepository.findByTitle(title).orElseThrow(
				()->new ResourceNotFoundException("Recipe","title",title));
		return RecipeMapper.mapToRecipeDto(recipe, new RecipeDto());
	}
	@Override
	public boolean updateRecipe(Long id,RecipeDto recipeDto) {
		if (recipeDto == null || id == null) {
	        return false;
	    }

	    RecipeEntity recipe = recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
	         "Recipe", "id", id.toString()
	    ));

	    recipe.setTitle(recipeDto.getTitle());
	    recipe.setInstructions(recipeDto.getInstructions());
	    recipe.setImage(recipeDto.getImage());

	    Set<IngredientEntity> ingredients = recipeDto.getIngredients().stream()
	            .map(i -> ingredientRepository.findByName(i.getName())
	                    .orElseGet(() -> {
	                        IngredientEntity ingredient = new IngredientEntity();
	                        ingredient.setName(i.getName());
	                        return ingredientRepository.save(ingredient);
	                    }))
	            .collect(Collectors.toSet());

	    recipe.setIngredients(ingredients);

	    recipeRepository.save(recipe);
	    return true;
	}
	@Override
	public boolean deleteRecipe(Long id) {
		RecipeEntity recipe = recipeRepository.findById(id).orElseThrow(
			()-> new ResourceNotFoundException("Recipe","id",id.toString())
		);
		recipeRepository.deleteById(id);
		return true;
	}

}
