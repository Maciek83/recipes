package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.*;
import com.mgosciminski.recipe.model.CategoryDto;
import com.mgosciminski.recipe.model.IngredientDto;
import com.mgosciminski.recipe.model.RecipeDto;
import com.mgosciminski.recipe.model.UnitOfMeasureDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class RecipeDtoToRecipeTest {

    @Mock
    NoteDtoToNote noteDtoToNote;
    @Mock
    IngredientDtoToIngredient ingredientDtoToIngredient;
    @Mock
    CategoryDtoToCategory categoryDtoToCategory;
    @Mock
    UomDtoToUnitOfMeasure uomDtoToUnitOfMeasure;

    @InjectMocks
    RecipeDtoToRecipe recipeDtoToRecipe;

    @Test
    public void convertNull()
    {
        Recipe resultNull = recipeDtoToRecipe.convert(null);

        assertNull(resultNull);

        verifyZeroInteractions(noteDtoToNote);
        verifyZeroInteractions(ingredientDtoToIngredient);
        verifyZeroInteractions(categoryDtoToCategory);
    }

    @Test
    public void convert() {
        //given
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setDescription("desc");
        recipeDto.setDifficulty("EASY");


        //when
        when(noteDtoToNote.convert(any())).thenReturn(new Note());
        Recipe result = recipeDtoToRecipe.convert(recipeDto);

        //then
        assertNotNull(result);
        assertEquals(recipeDto.getDescription(),result.getDescription());

        verify(noteDtoToNote).convert(any());

    }
}