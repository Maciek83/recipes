package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.model.IngredientDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientDtoToIngredient implements Converter<IngredientDto, Ingredient> {

    // todo need recipe repository to glue recipe to ingredient
    // todo add test
    private final UomDToToUnitOfMeasure uomConverter;

    public IngredientDtoToIngredient(UomDToToUnitOfMeasure uomConverter) {
        this.uomConverter = uomConverter;
    }

    @Nullable
    @Override
    public Ingredient convert(IngredientDto ingredientDto) {

        if (ingredientDto == null)
        {
            return null;
        }

        Ingredient ingredient = new Ingredient();
//        ingredient.setRecipe();
        ingredient.setUnitOfMeasure(uomConverter.convert(ingredientDto.getUnitOfMeasureDto()));
        ingredient.setAmount(ingredientDto.getAmount());
        ingredient.setDescription(ingredientDto.getDescription());

        return ingredient;
    }
}
