package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.model.IngredientDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientToIngredientDto implements Converter<Ingredient, IngredientDto> {


    @Nullable
    @Override
    public synchronized IngredientDto convert(Ingredient ingredient) {

        if(ingredient == null)
        {
            return null;
        }

        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(ingredient.getId());
        ingredientDto.setDescription(ingredient.getDescription());
        ingredientDto.setUnitOfMeasure(ingredient.getUnitOfMeasure().getUom());
        ingredientDto.setAmount(ingredient.getAmount());
        ingredientDto.setRecipeId(ingredient.getRecipe().getId());

        return ingredientDto;
    }
}
