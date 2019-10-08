package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.*;
import com.mgosciminski.recipe.model.CategoryDto;
import com.mgosciminski.recipe.model.IngredientDto;
import com.mgosciminski.recipe.model.RecipeDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class RecipeDtoToRecipe implements Converter<RecipeDto,Recipe> {

    private final NoteDtoToNote noteDtoToNote;
    private final IngredientDtoToIngredient ingredientDtoToIngredient;
    private final CategoryDtoToCategory categoryDtoToCategory;
    private final UomDtoToUnitOfMeasure uomDtoToUnitOfMeasure;


    public RecipeDtoToRecipe(NoteDtoToNote noteDtoToNote,
                             IngredientDtoToIngredient ingredientDtoToIngredient,
                             CategoryDtoToCategory categoryDtoToCategory, UomDtoToUnitOfMeasure uomDtoToUnitOfMeasure) {
        this.noteDtoToNote = noteDtoToNote;
        this.ingredientDtoToIngredient = ingredientDtoToIngredient;
        this.categoryDtoToCategory = categoryDtoToCategory;
        this.uomDtoToUnitOfMeasure = uomDtoToUnitOfMeasure;
    }

    @Nullable
    @Override
    public synchronized Recipe convert(RecipeDto recipeDto) {

        if(recipeDto == null)
        {
            return null;
        }

        Recipe recipe = new Recipe();
        recipe.setDescription(recipeDto.getDescription());
        recipe.setPrepTime(recipeDto.getPrepTime());
        recipe.setCookTime(recipeDto.getCookTime());
        recipe.setServings(recipeDto.getServings());
        recipe.setSource(recipeDto.getSource());
        recipe.setUrl(recipeDto.getUrl());
        recipe.setDirections(recipeDto.getDirections());
        recipe.setDifficulty(Difficulty.valueOf(recipeDto.getDifficulty()));
        recipe.setNotes(noteDtoToNote.convert(recipeDto.getNoteDto()));


        return recipe;
    }
}
