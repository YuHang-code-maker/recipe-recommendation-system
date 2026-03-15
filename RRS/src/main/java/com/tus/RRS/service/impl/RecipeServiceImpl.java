package com.tus.RRS.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
		recipe.setExternalLink(recipeDto.getExternalLink());
		recipeRepository.save(recipe);
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
	    recipe.setExternalLink(recipeDto.getExternalLink());
	    recipeRepository.save(recipe);
	    return true;
	}
	@Override
	public boolean deleteRecipe(Long id) {
		RecipeEntity recipe = recipeRepository.findById(id).orElseThrow(
			()-> new ResourceNotFoundException("Recipe","id",id.toString()));
		recipeRepository.deleteById(id);
		return true;
	}
	@Override
	public List<RecipeDto> getAllRecipes() {
		List<RecipeEntity> recipes = recipeRepository.findAll();
		List<RecipeDto> recipeDtos = new ArrayList<>();

		for(RecipeEntity recipe : recipes){
		    recipeDtos.add(
		        RecipeMapper.mapToRecipeDto(recipe, new RecipeDto())
		    );
		}
		return recipeDtos;
	}
	@Override
	public RecipeDto getRecipeById(Long id) {
		RecipeEntity recipe = recipeRepository.findById(id).orElseThrow(
				()-> new ResourceNotFoundException("Recipe","id",id.toString()));
		return RecipeMapper.mapToRecipeDto(recipe, new RecipeDto());
	}
	@Override
	public List<RecipeDto> searchRecipesByTitle(String title) {
		List<RecipeEntity> recipes = recipeRepository.findByTitleContainingIgnoreCase(title);
		List<RecipeDto> recipeDtos = new ArrayList<>();

		for(RecipeEntity recipe : recipes){
		    recipeDtos.add(
		        RecipeMapper.mapToRecipeDto(recipe, new RecipeDto())
		    );
		}
		return recipeDtos;
	}
	@Override
	public List<RecipeDto> searchByIngredients(String ingredients) {
		List<String> selectedIngredients = Arrays.asList(ingredients.split(","));
	    List<RecipeEntity> allRecipes = recipeRepository.findAll();
	    List<RecipeDto> result = new ArrayList<>();
	    for (RecipeEntity recipe : allRecipes) {
	        int matchCount = 0;
	        for (IngredientEntity ingredient : recipe.getIngredients()) {
	            for (String selected : selectedIngredients) {
	                if (ingredient.getName().equalsIgnoreCase(selected.trim())) {
	                    matchCount++;
	                }
	            }
	        }
	        if (matchCount > 0) {
	        	RecipeDto dto = RecipeMapper.mapToRecipeDto(recipe, new RecipeDto());
	            dto.setMatchCount(matchCount);
	            result.add(dto);
	        }
	    }
	    
	    for (int i = 0; i < result.size() - 1; i++) {
	        for (int j = i + 1; j < result.size(); j++) {
	            if (result.get(j).getMatchCount() > result.get(i).getMatchCount()) {
	                RecipeDto temp = result.get(i);
	                result.set(i, result.get(j));
	                result.set(j, temp);
	            }
	        }
	    }
	    return result;
	}
}
