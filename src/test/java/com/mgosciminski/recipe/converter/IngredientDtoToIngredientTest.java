package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.model.IngredientDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
public class IngredientDtoToIngredientTest {


    private IngredientDtoToIngredient converter;

    @Before
    public void setUp()
    {
        converter = new IngredientDtoToIngredient();
    }

    @Test
    public void convertNull() throws Exception
    {
        Ingredient ingredientNull = converter.convert(null);

        assertNull(ingredientNull);
    }

    @Test
    public void convert() throws Exception
    {

        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setDescription("ingredient");

        Ingredient ingredient = converter.convert(ingredientDto);

        assertNotNull(ingredient);
        assertEquals(ingredient.getDescription(),"ingredient");

    }
}