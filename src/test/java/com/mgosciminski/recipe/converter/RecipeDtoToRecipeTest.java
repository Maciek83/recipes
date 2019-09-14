package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.Category;
import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.domain.Note;
import com.mgosciminski.recipe.domain.Recipe;
import com.mgosciminski.recipe.model.CategoryDto;
import com.mgosciminski.recipe.model.IngredientDto;
import com.mgosciminski.recipe.model.RecipeDto;
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
        Set<IngredientDto> ingredientDtos = new HashSet<>();
        ingredientDtos.add(new IngredientDto());
        ingredientDtos.add(new IngredientDto());
        recipeDto.setIngredientDtos(ingredientDtos);
        Set<CategoryDto> categoryDtos = new HashSet<>();
        categoryDtos.add(new CategoryDto());
        categoryDtos.add(new CategoryDto());
        recipeDto.setCategoryDtos(categoryDtos);

        //when
        when(noteDtoToNote.convert(any())).thenReturn(new Note());
        when(ingredientDtoToIngredient.convert(any())).thenReturn(new Ingredient());
        when(categoryDtoToCategory.convert(any())).thenReturn(new Category());
        Recipe result = recipeDtoToRecipe.convert(recipeDto);

        //then
        assertNotNull(result);
        assertEquals(recipeDto.getDescription(),result.getDescription());

        verify(noteDtoToNote).convert(any());
        verify(ingredientDtoToIngredient,times(2)).convert(any());
        verify(categoryDtoToCategory,times(2)).convert(any());
    }
}