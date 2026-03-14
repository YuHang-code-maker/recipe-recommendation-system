package com.tus.RRS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tus.RRS.constants.RecipeConstants;
import com.tus.RRS.dto.RecipeDto;
import com.tus.RRS.dto.ResponseDto;
import com.tus.RRS.entity.RecipeEntity;
import com.tus.RRS.service.IRecipeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping(path="/api/recipes", produces=MediaType.APPLICATION_JSON_VALUE)
@Validated
public class RecipeController {
	@Autowired
	private IRecipeService iRecipeService;
	
	@PostMapping
	public ResponseEntity<ResponseDto> createRecipe(@Valid @RequestBody RecipeDto recipeDto){
		iRecipeService.createRecipe(recipeDto);
		return ResponseEntity.status(HttpStatus.CREATED)
							.body(new ResponseDto(RecipeConstants.STATUS_201,RecipeConstants.MESSAGE_201));
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<RecipeDto>> searchRecipeByTitle(@RequestParam
			@NotBlank(message="Title cannot be empty")
			String title){
		List<RecipeDto> recipeDto = iRecipeService.searchRecipesByTitle(title);
		return ResponseEntity.status(HttpStatus.OK).body(recipeDto);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ResponseDto> updateRecipeDetails(@PathVariable 
			@Positive(message = "Id must be greater than 0")
			Long id,@Valid @RequestBody RecipeDto recipeDto){
		boolean isUpdated = iRecipeService.updateRecipe(id,recipeDto);
		if(isUpdated) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(RecipeConstants.STATUS_200,RecipeConstants.MESSAGE_200));
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto(RecipeConstants.STATUS_500,RecipeConstants.MESSAGE_500));
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseDto> deleteRecipeDetails(@PathVariable @Positive(message = "Id must be greater than 0") Long id){
		boolean isDeleted = iRecipeService.deleteRecipe(id);
		if(isDeleted) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(RecipeConstants.STATUS_200,RecipeConstants.MESSAGE_200));
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto(RecipeConstants.STATUS_500,RecipeConstants.MESSAGE_500));
		}
	}
	
	@GetMapping
	public ResponseEntity<List<RecipeDto>> getAllRecipes(){
		List<RecipeDto> recipes = iRecipeService.getAllRecipes();
		return ResponseEntity.status(HttpStatus.OK).body(recipes);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<RecipeDto> getRecipeById(@PathVariable @Positive(message = "Id must be greater than 0") Long id){
		RecipeDto recipeDto = iRecipeService.getRecipeById(id);
		return ResponseEntity.status(HttpStatus.OK).body(recipeDto);
	}
}
