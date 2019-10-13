package com.mgosciminski.recipe.controller;


import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.domain.Note;
import com.mgosciminski.recipe.domain.Recipe;
import com.mgosciminski.recipe.service.*;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
public class RecipeControllerTest {

    @Mock
    RecipeService recipeService;

    @Mock
    UomService uomService;

    @Mock
    IngredientService ingredientService;

    @Mock
    CategoryService categoryService;

    @Mock
    NoteService noteService;

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
    public void testMockMvcGetRecipes() throws Exception {
        mockMvc.perform(get("/recipe"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipes"))
                .andExpect(view().name("recipe/index"));
    }

    @Test
    public void goToFormAddRecipe() throws Exception {
        mockMvc.perform(get("/recipe/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("diff"))
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name(REC_FORM));

    }

    @Test
    public void postAddRecipeWithErrors() throws Exception {
        mockMvc.perform(post("/recipe/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(model().attributeExists("diff"))
                .andExpect(status().isOk())
                .andExpect(view().name(REC_FORM));
    }


    @Test
    public void postAddRecipeOk() throws Exception {
        mockMvc.perform(post("/recipe/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("description", "description")
                .param("prepTime", "100")
                .param("cookTime", "100")
                .param("servings", "100")
                .param("source", "source")
                .param("url", "http://test.pl")
                .param("directions", "directions")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe"));

        verify(recipeService).save(any());
    }

    @Test
    public void showIngredientsParseError() throws Exception {
        mockMvc.perform(get("/recipe/d/ingredients"))
                .andExpect(status().isNotFound())
                .andExpect(view().name(Error404));
    }

    @Test
    public void showIngredients() throws Exception {
        //given
        Recipe recipe = new Recipe();
        recipe.setIngredients(new HashSet<>());
        when(recipeService.findById(anyLong())).thenReturn(recipe);

        //when+then
        mockMvc.perform(get("/recipe/1/ingredients"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attributeExists("ingredients"))
                .andExpect(view().name("recipe/editIngredients/index"));

        verify(recipeService).findById(anyLong());
    }

    @Test
    public void goToAddIngredientFormParseError() throws Exception {
        mockMvc.perform(get("/recipe/a/ingredient/add"))
                .andExpect(status().isNotFound())
                .andExpect(view().name(Error404));
    }

    @Test
    public void goToAddIngredientForm() throws Exception {
        when(recipeService.findById(1L)).thenReturn(new Recipe());
        when(uomService.findAll()).thenReturn(new HashSet<>());

        mockMvc.perform(get("/recipe/1/ingredient/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uoms"))
                .andExpect(view().name("ingredient/form/index"));

        verify(recipeService).findById(anyLong());
        verify(uomService).findAll();
    }

    @Test
    public void addIngredientPostParseError() throws Exception {
        mockMvc.perform(post("/recipe/a/ingredient/add"))
                .andExpect(status().isNotFound())
                .andExpect(view().name(Error404));
    }

    @Test
    public void addIngredientWithFieldsError() throws Exception {

        when(recipeService.findById(anyLong())).thenReturn(new Recipe());
        when(uomService.findAll()).thenReturn(new HashSet<>());

        mockMvc.perform(post("/recipe/1/ingredient/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attributeExists("uoms"))
                .andExpect(view().name("ingredient/form/index"));

        verify(recipeService).findById(anyLong());
        verify(uomService).findAll();
    }

    @Test
    public void addIngredientAllGood() throws Exception {
        when(recipeService.findById(anyLong())).thenReturn(new Recipe());
        when(ingredientService.save(any())).thenReturn(new Ingredient());
        when(recipeService.save(any())).thenReturn(new Recipe());

        mockMvc.perform(post("/recipe/1/ingredient/add")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("description", "description")
                .param("amount", "100.00")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1"));

        verify(recipeService).findById(anyLong());
        verify(ingredientService).save(any());
        verify(recipeService).save(any());
    }

    @Test
    public void goToEditCategoryFormParseError() throws Exception {

        mockMvc.perform(get("/recipe/d/categories"))
                .andExpect(status().isNotFound())
                .andExpect(view().name(Error404));
    }

    @Test
    public void goToEditCategoryFormOk() throws Exception {
        when(recipeService.findById(anyLong())).thenReturn(new Recipe());
        when(categoryService.findAll()).thenReturn(new HashSet<>());

        mockMvc.perform(get("/recipe/1/categories"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attributeExists("recipeCat"))
                .andExpect(model().attributeExists("allcategories"))
                .andExpect(view().name("recipe/editCategories/index"));

        verify(recipeService).findById(anyLong());
        verify(categoryService).findAll();
    }

    @Test
    public void changeCategories() throws Exception {

        when(recipeService.findById(anyLong())).thenReturn(new Recipe());
        when(recipeService.save(any())).thenReturn(new Recipe());

        mockMvc.perform(post("/recipe/1/categories"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1"));

        verify(recipeService).findById(anyLong());
        verify(recipeService).save(any());

    }

    @Test
    public void showRecipeException() throws Exception {
        mockMvc.perform(get("/recipe/d"))
                .andExpect(status().isNotFound())
                .andExpect(view().name(Error404));
    }

    @Test
    public void showRecipe() throws Exception {
        when(recipeService.findById(anyLong())).thenReturn(new Recipe());

        mockMvc.perform(get("/recipe/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/show/index"));

        verify(recipeService).findById(anyLong());
    }

    @Test
    public void goToEditNoteFormException() throws Exception {
        mockMvc.perform(get("/recipe/a/note"))
                .andExpect(status().isNotFound())
                .andExpect(view().name(Error404));
    }

    @Test
    public void goToEditFormNoNote() throws Exception {
        Recipe recipe = new Recipe();
        when(recipeService.findById(anyLong())).thenReturn(recipe);

        mockMvc.perform(get("/recipe/1/note"))
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attributeExists("note"))
                .andExpect(model().attribute("note", hasProperty("description", is("Add description"))))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/editNote/index"));


        verify(recipeService).findById(anyLong());
    }

    @Test
    public void goToEditFormRecipeWithNote() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setNotes(new Note());
        when(recipeService.findById(anyLong())).thenReturn(recipe);

        mockMvc.perform(get("/recipe/1/note"))
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attributeExists("note"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/editNote/index"));


        verify(recipeService).findById(anyLong());
    }

    @Test
    public void addNotePostParseError() throws Exception {
        mockMvc.perform(post("/recipe/a/note"))
                .andExpect(status().isNotFound())
                .andExpect(view().name(Error404));
    }

    @Test
    public void addNotePostNoteError() throws Exception {
        when(recipeService.findById(anyLong())).thenReturn(new Recipe());

        mockMvc.perform(post("/recipe/1/note"))
                .andExpect(model().attributeExists("recipe"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/editNote/index"));

        verify(recipeService).findById(anyLong());
    }

    @Test
    public void addNotePostOk() throws Exception {
        when(recipeService.findById(anyLong())).thenReturn(new Recipe());
        when(recipeService.save(any())).thenReturn(new Recipe());
        when(noteService.save(any())).thenReturn(new Note());

        mockMvc.perform(post("/recipe/1/note")
                .param("description", "description")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1"));

        verify(recipeService).findById(anyLong());
        verify(recipeService).save(any());
        verify(noteService).save(any());
    }

    @Test
    public void goToEditRecipeFormParseError() throws Exception {
        mockMvc.perform(get("/recipe/d/edit"))
                .andExpect(status().isNotFound())
                .andExpect(view().name(Error404));
    }

    @Test
    public void goToEditRecipeForm() throws Exception {
        when(recipeService.findById(anyLong())).thenReturn(new Recipe());

        mockMvc.perform(get("/recipe/1/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(model().attributeExists("diff"))
                .andExpect(view().name("recipe/edit/index"));

        verify(recipeService).findById(anyLong());
    }

    @Test
    public void editRecipePostParseError() throws Exception {
        mockMvc.perform(post("/recipe/xx/edit"))
                .andExpect(status().isNotFound())
                .andExpect(view().name(Error404));
    }

    @Test
    public void editRecipePostRecipeWrongValues() throws Exception {


        mockMvc.perform(post("/recipe/1/edit")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("diff"))
                .andExpect(view().name("recipe/edit/index"));
    }

    @Test
    public void editRecipePostRecipeOk() throws Exception {

        when(recipeService.edit(any())).thenReturn(new Recipe());

        mockMvc.perform(post("/recipe/1/edit")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("description", "description")
                .param("prepTime", "100")
                .param("cookTime", "100")
                .param("servings", "100")
                .param("source", "source")
                .param("url", "http://test.pl")
                .param("directions", "directions")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/1"));

        verify(recipeService).edit(any());
    }

    @Test
    public void deleteRecipeParseError() throws Exception
    {
        mockMvc.perform(get("/recipe/d/delete"))
                .andExpect(status().isNotFound())
                .andExpect(view().name(Error404));
    }

    @Test
    public void deleteRecipeOk() throws Exception
    {
        mockMvc.perform(get("/recipe/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe"));

        verify(recipeService).deleteById(anyLong());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFound() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(Error404);
        return modelAndView;
    }


}