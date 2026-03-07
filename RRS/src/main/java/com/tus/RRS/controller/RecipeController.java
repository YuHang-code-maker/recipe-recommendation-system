package com.tus.RRS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
