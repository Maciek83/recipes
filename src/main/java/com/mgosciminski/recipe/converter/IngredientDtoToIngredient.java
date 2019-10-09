package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.IngredientDto;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import com.mgosciminski.recipe.service.UomService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientDtoToIngredient implements Converter<IngredientDto, Ingredient> {

    private final UomDtoToUnitOfMeasure uomDtoToUnitOfMeasure;

    public IngredientDtoToIngredient(UomDtoToUnitOfMeasure uomDtoToUnitOfMeasure) {
        this.uomDtoToUnitOfMeasure = uomDtoToUnitOfMeasure;
    }

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
