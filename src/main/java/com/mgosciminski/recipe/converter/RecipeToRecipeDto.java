package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.Category;
import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.domain.Recipe;
import com.mgosciminski.recipe.model.CategoryDto;
import com.mgosciminski.recipe.model.IngredientDto;
import com.mgosciminski.recipe.model.RecipeDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class RecipeToRecipeDto implements Converter<Recipe, RecipeDto> {

    private final NoteToNoteDto noteToNoteDto;
    private final IngredientToIngredientDto ingredientToIngredientDto;
    private final CategoryToCategoryDto categoryToCategoryDto;

    public RecipeToRecipeDto(NoteToNoteDto noteToNoteDto, IngredientToIngredientDto ingredientToIngredientDto, CategoryToCategoryDto categoryToCategoryDto) {
        this.noteToNoteDto = noteToNoteDto;
        this.ingredientToIngredientDto = ingredientToIngredientDto;
        this.categoryToCategoryDto = categoryToCategoryDto;
    }

    @Nullable
    @Override
    public RecipeDto convert(Recipe recipe) {

        if(recipe == null)
        {
            return null;
        }

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(recipe.getId());
        recipeDto.setDescription(recipe.getDescription());
        recipeDto.setPrepTime(recipe.getPrepTime());
        recipeDto.setCookTime(recipe.getCookTime());
        recipeDto.setServings(recipe.getServings());
        recipeDto.setSource(recipe.getSource());
        recipeDto.setUrl(recipe.getUrl());
        recipeDto.setDirections(recipe.getDirections());
        recipeDto.setDifficulty(recipe.getDifficulty().toString());
        recipeDto.setNoteDto(noteToNoteDto.convert(recipe.getNotes()));

        Set<Ingredient> ingredients = recipe.getIngredients();
        Set<IngredientDto> ingredientDtos = new HashSet<>();
        ingredients.forEach(ingredient -> ingredientDtos.add(ingredientToIngredientDto.convert(ingredient)));

        recipeDto.setIngredientDtos(ingredientDtos);

        Set<Category> categories = recipe.getCategories();
        Set<CategoryDto> categoryDtos = new HashSet<>();
        categories.forEach(category -> categoryDtos.add(categoryToCategoryDto.convert(category)));

        recipeDto.setCategoryDtos(categoryDtos);

        return recipeDto;
    }
}
