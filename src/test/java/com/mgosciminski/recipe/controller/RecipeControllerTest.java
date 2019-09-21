package com.mgosciminski.recipe.controller;

import com.mgosciminski.recipe.domain.Recipe;
import com.mgosciminski.recipe.model.RecipeDto;
import com.mgosciminski.recipe.service.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
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

    private MockMvc mockMvc;

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
        //given
        LinkedList<RecipeDto> recipes = new LinkedList<>();
        recipes.add(new RecipeDto());
        recipes.add(new RecipeDto());

        when(recipeService.findAllDto()).thenReturn(recipes);

        //when
        ArgumentCaptor<LinkedList<RecipeDto>> argumentCaptor = ArgumentCaptor.forClass(LinkedList.class);
        String view = recipeController.showRecipes(model);

        //then
        assertNotNull(view);
        verify(recipeService).findAllDto();
        verify(model).addAttribute(eq("recipes"),argumentCaptor.capture());
        LinkedList<RecipeDto> recipes1 = argumentCaptor.getValue();
        assertEquals(recipes,recipes1);
        assertEquals(recipes1.size(),2);

    }
}