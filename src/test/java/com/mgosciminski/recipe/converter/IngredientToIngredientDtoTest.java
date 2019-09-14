package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.domain.Recipe;
import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.IngredientDto;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class IngredientToIngredientDtoTest {

    @Mock
    UnitOfMeasureToUomDto oumConverter;

    @InjectMocks
    IngredientToIngredientDto converter;

    @Test
    public void convertNull() throws Exception
    {
        assertNull(converter.convert(null));

        verifyZeroInteractions(oumConverter);
    }

    @Test
    public void convert() throws Exception
    {
        //given
        Ingredient ingredient = new Ingredient();
        ingredient.setDescription("description");

        Recipe recipe = new Recipe();
        recipe.setId(1L);
        ingredient.setRecipe(recipe);

        //when
        when(oumConverter.convert(any())).thenReturn(new UnitOfMeasureDto());

        IngredientDto ingredientDto = converter.convert(ingredient);

        //then
        assertNotNull(ingredientDto);
        assertEquals(ingredient.getDescription(),ingredientDto.getDescription());

    }
}