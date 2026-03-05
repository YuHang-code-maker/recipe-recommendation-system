package com.tus.RRS.entity;


import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "recipes")
public class RecipeEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String instructions;

    private String image;
    
    @ManyToMany
    @JoinTable(name = "recipe_ingredients",joinColumns = @JoinColumn(name = "recipe_id"),
    	    inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private Set<IngredientEntity> ingredients = new HashSet<>();
   
	public RecipeEntity() {}

	public RecipeEntity( String title, String instructions, String image) {
		this.title = title;
		this.instructions = instructions;
		this.image = image;
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

	public Set<IngredientEntity> getIngredients() {
		return ingredients;
	}

	public void setIngredients(Set<IngredientEntity> ingredients) {
		this.ingredients = ingredients;
	}
    
}

