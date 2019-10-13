package com.mgosciminski.recipe.controller;

import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.domain.Recipe;
import com.mgosciminski.recipe.service.IngredientService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
public class IngredientControllerTest {

    @Mock
    IngredientService ingredientService;

    @InjectMocks
    IngredientController ingredientController;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private MockMvc mockMvc;

    private final String NOT_FOUND = "can't find this id";
    private final String Error404 = "404error";

    @Before
    public void setup()
    {
        mockMvc = MockMvcBuilders
                .standaloneSetup(ingredientController)
                .build();
    }

    @Test
    public void deleteIngredient() throws Exception {

        //given
        Ingredient ingredient = new Ingredient();
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        ingredient.setRecipe(recipe);
        when(ingredientService.findById(anyLong())).thenReturn(ingredient);


        //when
        mockMvc.perform(get("/ingredient/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1/ingredients"));

        //then
        verify(ingredientService).findById(anyLong());
        verify(ingredientService).delete(any());

    }

    @Test
    public void deleteIngredientNumberFormatException() throws Exception {
        mockMvc.perform(get("/ingredient/a/delete"))
                .andExpect(status().isNotFound())
                .andExpect(view().name(Error404));
    }
}