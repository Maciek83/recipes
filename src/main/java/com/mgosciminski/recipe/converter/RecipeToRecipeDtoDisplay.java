package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.Recipe;
import com.mgosciminski.recipe.model.CategoryDto;
import com.mgosciminski.recipe.model.IngredientDto;
import com.mgosciminski.recipe.model.NoteDto;
import com.mgosciminski.recipe.model.RecipeDtoDisplay;
import javassist.NotFoundException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class RecipeToRecipeDtoDisplay implements Converter<Recipe, RecipeDtoDisplay> {

    private final NoteToNoteDto noteToNoteDto;
    private final IngredientToIngredientDto ingredientToIngredientDto;
    private final CategoryToCategoryDto categoryToCategoryDto;

    public RecipeToRecipeDtoDisplay(NoteToNoteDto noteToNoteDto, IngredientToIngredientDto ingredientToIngredientDto, CategoryToCategoryDto categoryToCategoryDto) {
        this.noteToNoteDto = noteToNoteDto;
        this.ingredientToIngredientDto = ingredientToIngredientDto;
        this.categoryToCategoryDto = categoryToCategoryDto;
    }

    @Override
    public RecipeDtoDisplay convert(Recipe recipe) {

        if(recipe == null)
        {
            return null;
        }

        RecipeDtoDisplay recipeDtoDisplay = new RecipeDtoDisplay();

        recipeDtoDisplay.setId(recipe.getId());
        recipeDtoDisplay.setDescription(recipe.getDescription());
        recipeDtoDisplay.setPrepTime(recipe.getPrepTime());
        recipeDtoDisplay.setCookTime(recipe.getCookTime());
        recipeDtoDisplay.setServings(recipe.getServings());
        recipeDtoDisplay.setSource(recipe.getSource());
        recipeDtoDisplay.setUrl(recipe.getUrl());
        recipeDtoDisplay.setDirections(recipe.getDirections());
        recipeDtoDisplay.setDifficulty(recipe.getDifficulty());

        NoteDto noteDto = noteToNoteDto.convert(recipe.getNotes());
        recipeDtoDisplay.setNotes(noteDto);

        List<IngredientDto> listIngredientDto = new LinkedList<>();
        recipe.getIngredients().forEach(ingredient -> listIngredientDto.add(ingredientToIngredientDto.convert(ingredient)));

        recipeDtoDisplay.setIngredients(listIngredientDto);

        List<CategoryDto> categoryDtos = new LinkedList<>();
        recipe.getCategories().forEach(category -> categoryDtos.add(categoryToCategoryDto.convert(category)) );

        recipeDtoDisplay.setCategoryDtos(categoryDtos);

        return recipeDtoDisplay;
    }
}
