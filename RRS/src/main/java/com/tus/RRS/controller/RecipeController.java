package com.tus.RRS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.tus.RRS.service.IRecipeService;

@RestController
@RequestMapping(path="/api", produces=MediaType.APPLICATION_JSON_VALUE)
public class RecipeController {
	@Autowired
	private IRecipeService iRecipeService;
	
	@PostMapping("/recipe")
	public ResponseEntity<ResponseDto> createRecipe(@RequestBody RecipeDto recipeDto){
		iRecipeService.createRecipe(recipeDto);
		return ResponseEntity.status(HttpStatus.CREATED)
							.body(new ResponseDto(RecipeConstants.STATUS_201,RecipeConstants.MESSAGE_201));
	}
	
	@GetMapping("/recipe")
	public ResponseEntity<RecipeDto> fetchRecipeDetails(@RequestParam String title){
		RecipeDto recipeDto = iRecipeService.fetchRecipe(title);
		return ResponseEntity.status(HttpStatus.OK).body(recipeDto);
	}
	
	@PutMapping("/recipe/{id}")
	public ResponseEntity<ResponseDto> updateRecipeDetails(@PathVariable Long id,@RequestBody RecipeDto recipeDto){
		boolean isUpdated = iRecipeService.updateRecipe(id,recipeDto);
		if(isUpdated) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(RecipeConstants.STATUS_200,RecipeConstants.MESSAGE_200));
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto(RecipeConstants.STATUS_500,RecipeConstants.MESSAGE_500));
		}
	}
	
	@DeleteMapping("/recipe/{id}")
	public ResponseEntity<ResponseDto> deleteRecipeDetails(@PathVariable Long id){
		boolean isDeleted = iRecipeService.deleteRecipe(id);
		if(isDeleted) {
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(RecipeConstants.STATUS_200,RecipeConstants.MESSAGE_200));
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto(RecipeConstants.STATUS_500,RecipeConstants.MESSAGE_500));
		}
	}
}
