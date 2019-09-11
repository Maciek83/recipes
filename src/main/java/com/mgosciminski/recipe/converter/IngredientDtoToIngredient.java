package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.model.IngredientDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientDtoToIngredient implements Converter<IngredientDto, Ingredient> {

    @Nullable
    @Override
    public synchronized Ingredient convert(IngredientDto ingredientDto) {

        if (ingredientDto == null)
        {
            return null;
        }

        Ingredient ingredient = new Ingredient();
        ingredient.setAmount(ingredientDto.getAmount());
        ingredient.setDescription(ingredientDto.getDescription());

        return ingredient;
    }
}
