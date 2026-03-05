package com.tus.RRS.dto;

import java.util.Set;



public class RecipeDto {
	private Long id;

    private String title;

    private String instructions;

    private String image;
    
    Set<IngredientDto> ingredients;

	public RecipeDto() {
	}

	public RecipeDto(Long id, String title, String instructions, String image, Set<IngredientDto> ingredients) {
		this.id = id;
		this.title = title;
		this.instructions = instructions;
		this.image = image;
		this.ingredients = ingredients;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}


	public Set<IngredientDto> getIngredients() {
		return ingredients;
	}


	public void setIngredients(Set<IngredientDto> ingredients) {
		this.ingredients = ingredients;
	}
    
    
}
