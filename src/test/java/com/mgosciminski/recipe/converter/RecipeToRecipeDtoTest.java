package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.*;
import com.mgosciminski.recipe.model.CategoryDto;
import com.mgosciminski.recipe.model.IngredientDto;
import com.mgosciminski.recipe.model.NoteDto;
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
public class RecipeToRecipeDtoTest {

    @Mock
    NoteToNoteDto noteToNoteDto;
    @Mock
    IngredientToIngredientDto ingredientToIngredientDto;
    @Mock
    CategoryToCategoryDto categoryToCategoryDto;

    @InjectMocks
    RecipeToRecipeDto recipeToRecipeDto;

    @Test
    public void convertNull()
    {
        RecipeDto recipeDto = recipeToRecipeDto.convert(null);

        assertNull(recipeDto);

        verifyZeroInteractions(noteToNoteDto);
        verifyZeroInteractions(ingredientToIngredientDto);
        verifyZeroInteractions(categoryToCategoryDto);
    }

    @Test
    public void convert() {

        //given
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setDescription("description");
        recipe.setPrepTime(2);
        recipe.setCookTime(3);
        recipe.setServings(4);
        recipe.setSource("source");
        recipe.setUrl("url");
        recipe.setDirections("directions");
        recipe.setDifficulty(Difficulty.MODERATE);
        recipe.setNotes(new Note());
        Set<Ingredient> ingredients = new HashSet<>();
        ingredients.add(new Ingredient());
        ingredients.add(new Ingredient());
        recipe.setIngredients(ingredients);
        Set<Category> categories = new HashSet<>();
        categories.add(new Category());
        categories.add(new Category());
        recipe.setCategories(categories);

        //when
        when(noteToNoteDto.convert(any(Note.class))).thenReturn(new NoteDto());
        when(ingredientToIngredientDto.convert(any(Ingredient.class))).thenReturn(new IngredientDto());
        when(categoryToCategoryDto.convert(any(Category.class))).thenReturn(new CategoryDto());

        RecipeDto recipeDto = recipeToRecipeDto.convert(recipe);

        //then
        assertNotNull(recipeDto);
        assertEquals(recipeDto.getDescription(),"description");

        verify(noteToNoteDto,times(1)).convert(any(Note.class));
        verify(ingredientToIngredientDto,times(2)).convert(any(Ingredient.class));
        verify(categoryToCategoryDto,times(2)).convert(any(Category.class));
    }
}