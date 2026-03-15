package com.tus.RRS.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class RecipeDto {
	private Long id;

	@NotBlank(message="Title cannot be empty")
    private String title;

    private String instructions;

    private String image;
    
    @NotEmpty(message="Recipe must contain at least one ingredient")
    Set<IngredientDto> ingredients;
    
    private String externalLink;
    
    private int matchCount;

	public RecipeDto() {
	}

	public RecipeDto(Long id, String title, String instructions, String image, Set<IngredientDto> ingredients,String externalLink) {
		this.id = id;
		this.title = title;
		this.instructions = instructions;
		this.image = image;
		this.ingredients = ingredients;
		this.externalLink = externalLink;
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
    
	public int getMatchCount() {
        return matchCount;
    }

    public void setMatchCount(int matchCount) {
        this.matchCount = matchCount;
    }

	public String getExternalLink() {
		return externalLink;
	}

	public void setExternalLink(String externalLink) {
		this.externalLink = externalLink;
	}

    
}
