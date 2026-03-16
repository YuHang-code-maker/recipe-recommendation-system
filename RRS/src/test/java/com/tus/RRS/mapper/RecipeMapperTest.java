package com.tus.RRS.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.tus.RRS.dto.IngredientDto;
import com.tus.RRS.dto.RecipeDto;
import com.tus.RRS.entity.IngredientEntity;
import com.tus.RRS.entity.RecipeEntity;

class RecipeMapperTest {

    @Test
    void mapToRecipeDtoShouldMapEntityToDtoCorrectly() {

        IngredientEntity ingredient1 = new IngredientEntity();
        ingredient1.setId(1L);
        ingredient1.setName("Tomato");

        IngredientEntity ingredient2 = new IngredientEntity();
        ingredient2.setId(2L);
        ingredient2.setName("Cheese");

        Set<IngredientEntity> ingredientEntities = new HashSet<>();
        ingredientEntities.add(ingredient1);
        ingredientEntities.add(ingredient2);

        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setId(10L);
        recipeEntity.setTitle("Pizza");
        recipeEntity.setInstructions("Bake it");
        recipeEntity.setImage("pizza.jpg");
        recipeEntity.setExternalLink("http://pizza.com");
        recipeEntity.setIngredients(ingredientEntities);

        RecipeDto result = RecipeMapper.mapToRecipeDto(recipeEntity, new RecipeDto());

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Pizza", result.getTitle());
        assertEquals("Bake it", result.getInstructions());
        assertEquals("pizza.jpg", result.getImage());
        assertEquals("http://pizza.com", result.getExternalLink());

        assertNotNull(result.getIngredients());
        assertEquals(2, result.getIngredients().size());
    }

    @Test
    void mapToRecipeEntityShouldMapDtoToEntityCorrectly() {

        IngredientDto ingredientDto1 = new IngredientDto(1L, "Tomato");
        IngredientDto ingredientDto2 = new IngredientDto(2L, "Cheese");

        Set<IngredientDto> ingredientDtos = new HashSet<>();
        ingredientDtos.add(ingredientDto1);
        ingredientDtos.add(ingredientDto2);

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(20L);
        recipeDto.setTitle("Pasta");
        recipeDto.setInstructions("Boil water");
        recipeDto.setImage("pasta.jpg");
        recipeDto.setExternalLink("http://pasta.com");
        recipeDto.setIngredients(ingredientDtos);

        RecipeEntity result = RecipeMapper.mapToRecipeEntity(recipeDto, new RecipeEntity());

        assertNotNull(result);
        assertEquals(20L, result.getId());
        assertEquals("Pasta", result.getTitle());
        assertEquals("Boil water", result.getInstructions());
        assertEquals("pasta.jpg", result.getImage());
        assertEquals("http://pasta.com", result.getExternalLink());

        assertNotNull(result.getIngredients());
        assertEquals(2, result.getIngredients().size());
    }
}
