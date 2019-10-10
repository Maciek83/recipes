package com.mgosciminski.recipe.controller;


import com.mgosciminski.recipe.domain.Recipe;
import com.mgosciminski.recipe.service.RecipeService;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
public class RecipeControllerTest {

    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    @InjectMocks
    RecipeController recipeController;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private MockMvc mockMvc;

    private final String Error404 = "404error";
    private final String NOT_FOUND = "can't find this id";
    private final String REC_FORM = "recipe/new/index";


    @Before
    public void setUp() throws Exception {

        mockMvc = MockMvcBuilders
                .standaloneSetup(recipeController)
                .build();
    }

    @Test
    public void testMockMvcGetRecipes() throws Exception
    {
        mockMvc.perform(get("/recipe"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipes"))
                .andExpect(view().name("recipe/index"));
    }

    @Test
    public void showRecipes() {


    }

    @Test
    public void showSingleRecipe() throws Exception {

        when(recipeService.findById(anyLong())).thenReturn(new Recipe());


        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/show/index"));
    }

    @Test
    public void showSingleRecipeNull() throws Exception
    {
        when(recipeService.findById(anyLong())).thenThrow(new NotFoundException(NOT_FOUND));

        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name(Error404));
    }

    @Test
    public void showSingleRecipeStringId() throws Exception
    {
        mockMvc.perform(get("/recipe/1r/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name(Error404));
    }

    @Test
    public void gotoCreateNewRecipeForm() throws Exception
    {
        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/new/index"));
    }


}