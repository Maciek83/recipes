package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.domain.UnitOfMeasure;
import com.mgosciminski.recipe.model.IngredientDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
public class IngredientDtoToIngredientTest {

    @Mock
    UomDtoToUnitOfMeasure uomDtoToUnitOfMeasure;

    @InjectMocks
    IngredientDtoToIngredient converter;


    @Test
    public void convertNull() throws Exception
    {
        Ingredient ingredientNull = converter.convert(null);

        assertNull(ingredientNull);

        verifyZeroInteractions(uomDtoToUnitOfMeasure);
    }

    @Test
    public void convert() throws Exception
    {
        //given
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setDescription("ingredient");

        //when
        when(uomDtoToUnitOfMeasure.convert(any())).thenReturn(new UnitOfMeasure());
        Ingredient ingredient = converter.convert(ingredientDto);

        //then
        assertNotNull(ingredient);
        assertEquals(ingredient.getDescription(),"ingredient");

        verify(uomDtoToUnitOfMeasure).convert(any());

    }
}