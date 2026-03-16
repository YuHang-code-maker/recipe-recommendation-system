package com.tus.RRS.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.tus.RRS.dto.IngredientDto;
import com.tus.RRS.dto.RecipeDto;
import com.tus.RRS.entity.IngredientEntity;
import com.tus.RRS.entity.RecipeEntity;
import com.tus.RRS.exception.DuplicateResourceException;
import com.tus.RRS.exception.ResourceNotFoundException;
import com.tus.RRS.repository.IngredientRepository;
import com.tus.RRS.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    private RecipeDto recipeDto;
    private RecipeEntity recipeEntity;
    private IngredientDto ingredientDto1;
    private IngredientDto ingredientDto2;
    private IngredientEntity ingredientEntity1;
    private IngredientEntity ingredientEntity2;

    @BeforeEach
    void setUp() {
        ingredientDto1 = new IngredientDto();
        ingredientDto1.setId(1L);
        ingredientDto1.setName("Tomato");

        ingredientDto2 = new IngredientDto();
        ingredientDto2.setId(2L);
        ingredientDto2.setName("Cheese");

        Set<IngredientDto> ingredientDtos = new HashSet<>();
        ingredientDtos.add(ingredientDto1);
        ingredientDtos.add(ingredientDto2);

        recipeDto = new RecipeDto();
        recipeDto.setId(1L);
        recipeDto.setTitle("Pizza");
        recipeDto.setInstructions("Bake it");
        recipeDto.setImage("pizza.jpg");
        recipeDto.setExternalLink("http://test.com");
        recipeDto.setIngredients(ingredientDtos);

        ingredientEntity1 = new IngredientEntity();
        ingredientEntity1.setId(1L);
        ingredientEntity1.setName("Tomato");

        ingredientEntity2 = new IngredientEntity();
        ingredientEntity2.setId(2L);
        ingredientEntity2.setName("Cheese");

        Set<IngredientEntity> ingredientEntities = new HashSet<>();
        ingredientEntities.add(ingredientEntity1);
        ingredientEntities.add(ingredientEntity2);

        recipeEntity = new RecipeEntity();
        recipeEntity.setId(1L);
        recipeEntity.setTitle("Pizza");
        recipeEntity.setInstructions("Bake it");
        recipeEntity.setImage("pizza.jpg");
        recipeEntity.setExternalLink("http://test.com");
        recipeEntity.setIngredients(ingredientEntities);
    }

    @Test
    void createRecipeShouldSaveRecipeSuccessfully() {
        when(recipeRepository.existsByTitle(recipeDto.getTitle())).thenReturn(false);
        when(ingredientRepository.findByName("Tomato")).thenReturn(Optional.of(ingredientEntity1));
        when(ingredientRepository.findByName("Cheese")).thenReturn(Optional.of(ingredientEntity2));

        recipeService.createRecipe(recipeDto);

        verify(recipeRepository, times(1)).save(any(RecipeEntity.class));
        verify(ingredientRepository, never()).save(any(IngredientEntity.class));
    }

    @Test
    void createRecipeShouldThrowExceptionWhenTitleAlreadyExists() {
        when(recipeRepository.existsByTitle(recipeDto.getTitle())).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> recipeService.createRecipe(recipeDto));

        verify(recipeRepository, never()).save(any(RecipeEntity.class));
        verify(ingredientRepository, never()).save(any(IngredientEntity.class));
    }

    @Test
    void createRecipeShouldSaveNewIngredientWhenIngredientDoesNotExist() {
        when(recipeRepository.existsByTitle(recipeDto.getTitle())).thenReturn(false);
        when(ingredientRepository.findByName("Tomato")).thenReturn(Optional.of(ingredientEntity1));
        when(ingredientRepository.findByName("Cheese")).thenReturn(Optional.empty());
        when(ingredientRepository.save(any(IngredientEntity.class))).thenReturn(ingredientEntity2);

        recipeService.createRecipe(recipeDto);

        verify(ingredientRepository, times(1)).save(any(IngredientEntity.class));
        verify(recipeRepository, times(1)).save(any(RecipeEntity.class));
    }

    @Test
    void updateRecipeShouldReturnFalseWhenIdIsNull() {
        boolean result = recipeService.updateRecipe(null, recipeDto);

        assertFalse(result);
        verify(recipeRepository, never()).findById(any());
        verify(recipeRepository, never()).save(any());
    }

    @Test
    void updateRecipeShouldReturnFalseWhenRecipeDtoIsNull() {
        boolean result = recipeService.updateRecipe(1L, null);

        assertFalse(result);
        verify(recipeRepository, never()).findById(any());
        verify(recipeRepository, never()).save(any());
    }

    @Test
    void updateRecipeShouldThrowExceptionWhenRecipeNotFound() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recipeService.updateRecipe(1L, recipeDto));

        verify(recipeRepository, never()).save(any());
    }

    @Test
    void updateRecipeShouldReturnTrueWhenRecipeExists() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipeEntity));
        when(ingredientRepository.findByName("Tomato")).thenReturn(Optional.of(ingredientEntity1));
        when(ingredientRepository.findByName("Cheese")).thenReturn(Optional.of(ingredientEntity2));
        when(recipeRepository.save(any(RecipeEntity.class))).thenReturn(recipeEntity);

        boolean result = recipeService.updateRecipe(1L, recipeDto);

        assertTrue(result);
        assertEquals("Pizza", recipeEntity.getTitle());
        assertEquals("Bake it", recipeEntity.getInstructions());
        assertEquals("pizza.jpg", recipeEntity.getImage());
        assertEquals("http://test.com", recipeEntity.getExternalLink());
        verify(recipeRepository, times(1)).save(recipeEntity);
    }

    @Test
    void updateRecipeShouldSaveNewIngredientWhenIngredientDoesNotExist() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipeEntity));
        when(ingredientRepository.findByName("Tomato")).thenReturn(Optional.of(ingredientEntity1));
        when(ingredientRepository.findByName("Cheese")).thenReturn(Optional.empty());
        when(ingredientRepository.save(any(IngredientEntity.class))).thenReturn(ingredientEntity2);

        boolean result = recipeService.updateRecipe(1L, recipeDto);

        assertTrue(result);
        verify(ingredientRepository, times(1)).save(any(IngredientEntity.class));
        verify(recipeRepository, times(1)).save(any(RecipeEntity.class));
    }

    @Test
    void deleteRecipeShouldReturnTrueWhenRecipeExists() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipeEntity));

        boolean result = recipeService.deleteRecipe(1L);

        assertTrue(result);
        verify(recipeRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteRecipeShouldThrowExceptionWhenRecipeNotFound() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recipeService.deleteRecipe(1L));

        verify(recipeRepository, never()).deleteById(any());
    }

    @Test
    void getAllRecipesShouldReturnAllRecipes() {
        when(recipeRepository.findAll()).thenReturn(List.of(recipeEntity));

        List<RecipeDto> result = recipeService.getAllRecipes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Pizza", result.get(0).getTitle());
    }

    @Test
    void getRecipeByIdShouldReturnRecipeWhenExists() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipeEntity));

        RecipeDto result = recipeService.getRecipeById(1L);

        assertNotNull(result);
        assertEquals("Pizza", result.getTitle());
        assertEquals("Bake it", result.getInstructions());
    }

    @Test
    void getRecipeByIdShouldThrowExceptionWhenRecipeNotFound() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recipeService.getRecipeById(1L));
    }

    @Test
    void searchRecipesByTitleShouldReturnMatchingRecipes() {
        when(recipeRepository.findByTitleContainingIgnoreCase("Piz")).thenReturn(List.of(recipeEntity));

        List<RecipeDto> result = recipeService.searchRecipesByTitle("Piz");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Pizza", result.get(0).getTitle());
    }

    @Test
    void searchRecipesByTitleShouldReturnEmptyListWhenNoMatch() {
        when(recipeRepository.findByTitleContainingIgnoreCase("Burger")).thenReturn(List.of());

        List<RecipeDto> result = recipeService.searchRecipesByTitle("Burger");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void searchByIngredientsShouldReturnRecipesSortedByMatchCount() {
        IngredientEntity tomato = new IngredientEntity();
        tomato.setId(1L);
        tomato.setName("Tomato");

        IngredientEntity cheese = new IngredientEntity();
        cheese.setId(2L);
        cheese.setName("Cheese");

        IngredientEntity onion = new IngredientEntity();
        onion.setId(3L);
        onion.setName("Onion");

        RecipeEntity recipe1 = new RecipeEntity();
        recipe1.setId(1L);
        recipe1.setTitle("Pizza");
        recipe1.setIngredients(Set.of(tomato, cheese));

        RecipeEntity recipe2 = new RecipeEntity();
        recipe2.setId(2L);
        recipe2.setTitle("Salad");
        recipe2.setIngredients(Set.of(tomato));

        RecipeEntity recipe3 = new RecipeEntity();
        recipe3.setId(3L);
        recipe3.setTitle("Soup");
        recipe3.setIngredients(Set.of(onion));

        when(recipeRepository.findAll()).thenReturn(List.of(recipe1, recipe2, recipe3));

        List<RecipeDto> result = recipeService.searchByIngredients("Tomato,Cheese");

        assertEquals(2, result.size());
        assertEquals("Pizza", result.get(0).getTitle());
        assertEquals(2, result.get(0).getMatchCount());
        assertEquals("Salad", result.get(1).getTitle());
        assertEquals(1, result.get(1).getMatchCount());
    }

    @Test
    void searchByIngredientsShouldReturnEmptyListWhenNoIngredientMatches() {
        when(recipeRepository.findAll()).thenReturn(List.of(recipeEntity));

        List<RecipeDto> result = recipeService.searchByIngredients("Beef");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}