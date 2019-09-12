package com.mgosciminski.recipe.model;

import java.util.HashSet;
import java.util.Set;

public class RecipeDto {

    private Long id;
    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    private String directions;
    private String difficulty;
    private NoteDto noteDto;
    private Set<IngredientDto> ingredientDtos = new HashSet<>();
    private Set<CategoryDto> categoryDtos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }

    public Integer getCookTime() {
        return cookTime;
    }

    public void setCookTime(Integer cookTime) {
        this.cookTime = cookTime;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public NoteDto getNoteDto() {
        return noteDto;
    }

    public void setNoteDto(NoteDto noteDto) {
        this.noteDto = noteDto;
    }

    public Set<IngredientDto> getIngredientDtos() {
        return ingredientDtos;
    }

    public void setIngredientDtos(Set<IngredientDto> ingredientDtos) {
        this.ingredientDtos = ingredientDtos;
    }

    public Set<CategoryDto> getCategoryDtos() {
        return categoryDtos;
    }

    public void setCategoryDtos(Set<CategoryDto> categoryDtos) {
        this.categoryDtos = categoryDtos;
    }
}
