package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.converter.CategoryDtoToCategory;
import com.mgosciminski.recipe.converter.IngredientDtoToIngredient;
import com.mgosciminski.recipe.converter.NoteDtoToNote;
import com.mgosciminski.recipe.converter.RecipeDtoToRecipe;
import com.mgosciminski.recipe.domain.*;
import com.mgosciminski.recipe.model.CategoryDto;
import com.mgosciminski.recipe.model.IngredientDto;
import com.mgosciminski.recipe.model.NoteDto;
import com.mgosciminski.recipe.model.RecipeDto;
import com.mgosciminski.recipe.repository.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class RecipeServiceImplTest {

    @Mock
    RecipeRepository repository;
    @Mock
    RecipeDtoToRecipe recipeDtoToRecipe;
    @Mock
    NoteServiceImpl noteService;
    @Mock
    IngredientServiceImpl ingredientService;
    @Mock
    CategoryServiceImpl categoryService;
    @Mock
    UomService uomService;


    @InjectMocks
    RecipeServiceImpl service;

    private RecipeServiceImpl serviceSpy;

    private Set<Recipe> recipes;

    private final Long BAD = -1L;

    @Before
    public void setUp() throws Exception {

        recipes = new HashSet<>();
        Recipe recipe = new Recipe();
        recipe.setDifficulty(Difficulty.HARD);
        recipes.add(recipe);
        recipes.add(new Recipe());

        serviceSpy = Mockito.spy(service);

    }

    @Test
    public void saveRecipe() {
        //given
        Recipe recipe = new Recipe();

        //when
        when(repository.save(any())).thenReturn(recipe);
        Recipe result = service.save(recipe);

        //then
        assertNotNull(result);
        assertEquals(recipe,result);
    }

    @Test
    public void saveRecipeDto() {

        //given
        Recipe recipe = new Recipe();
        recipe.setDifficulty(Difficulty.HARD);
        recipe.setNotes(new Note());

        Set<Ingredient> ingredients = new HashSet<>();

        Ingredient ingredient = new Ingredient();
        UnitOfMeasure unitOfMeasure = new UnitOfMeasure();
        unitOfMeasure.getIngredients().add(ingredient);
        ingredient.setUnitOfMeasure(unitOfMeasure);
        ingredients.add(ingredient);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setDescription("aa");
        UnitOfMeasure unitOfMeasure1 = new UnitOfMeasure();
        unitOfMeasure1.setUom("aa");
        unitOfMeasure1.getIngredients().add(ingredient1);
        ingredient1.setUnitOfMeasure(unitOfMeasure1);
        ingredients.add(ingredient1);

        recipe.setIngredients(ingredients);

        Set<Category> categories = new HashSet<>();
        categories.add(new Category());
        categories.add(new Category());
        recipe.setCategories(categories);

        //when
        when(recipeDtoToRecipe.convert(any())).thenReturn(recipe);
        when(noteService.save(any(Note.class))).thenReturn(new Note());
        when(uomService.save(any(UnitOfMeasure.class))).thenReturn(new UnitOfMeasure());
        when(ingredientService.save(any(Ingredient.class))).thenReturn(new Ingredient());
        doReturn(recipe).when(serviceSpy).save(any(Recipe.class));
        Recipe result = serviceSpy.save(new RecipeDto());

        //then
        assertNotNull(result);
        assertEquals(recipe,result);


        verify(recipeDtoToRecipe).convert(any());
        verify(noteService).save(any(Note.class));
        verify(uomService,times(2)).save(any(UnitOfMeasure.class));
        verify(ingredientService,times(2)).save(any(Ingredient.class));
        verify(serviceSpy,times(2)).save(any(Recipe.class));

    }

    @Test
    public void findAll() {

        //when
        when(repository.findAll()).thenReturn(recipes);
        Set<Recipe> result = (Set<Recipe>) service.findAll();

        //then
        assertNotNull(result);
        assertEquals(result,recipes);
        assertEquals(result.size(),2);
    }

    @Test
    public void findByIdNull()
    {
        //given
        Optional<Recipe> nullRecipe = Optional.empty();

        //when
        when(repository.findById(anyLong())).thenReturn(nullRecipe);
        Recipe recipe = service.findById(1L);

        //then
        assertNotNull(recipe);
        assertEquals(recipe.getId(),BAD);
    }

    @Test
    public void findById() {
        //given
        Optional<Recipe> optionalRecipe = Optional.of(new Recipe());

        //when
        when(repository.findById(anyLong())).thenReturn(optionalRecipe);
        Recipe recipe = service.findById(1L);

        //then
        assertNotNull(recipe);
        assertEquals(recipe,optionalRecipe.get());
    }

    @Test
    public void delete() {

        service.delete(new Recipe());

        verify(repository).delete(any(Recipe.class));

    }

    @Test
    public void deleteById() {

        service.deleteById(1L);

        verify(repository).deleteById(anyLong());
    }

    @Test
    public void edit() {

        //given
        Recipe recipe = new Recipe();
        recipe.setId(1L);


        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(1L);
        recipeDto.setDifficulty("EASY");
        NoteDto noteDto = new NoteDto();
        noteDto.setId(1L);
        recipeDto.setNoteDto(noteDto);
        Set<IngredientDto> ingredientDtos = new HashSet<>();
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(1L);
        IngredientDto ingredientDto1 = new IngredientDto();
        ingredientDto1.setId(2L);
        ingredientDtos.add(ingredientDto);
        ingredientDtos.add(ingredientDto1);
        recipeDto.setIngredientDtos(ingredientDtos);
        Set<CategoryDto> categoryDtos = new HashSet<>();
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        CategoryDto categoryDto1 = new CategoryDto();
        categoryDto1.setId(2L);
        categoryDtos.add(categoryDto);
        categoryDtos.add(categoryDto1);
        recipeDto.setCategoryDtos(categoryDtos);


        //when
        doReturn(recipe).when(serviceSpy).findById(anyLong());
        when(noteService.findById(anyLong())).thenReturn(new Note());
        when(noteService.save(any(Note.class))).thenReturn(new Note());
        when(ingredientService.findById(anyLong())).thenReturn(new Ingredient());
        when(ingredientService.save(any(Ingredient.class))).thenReturn(new Ingredient());
        when(categoryService.findById(anyLong())).thenReturn(new Category());
        doReturn(recipe).when(serviceSpy).save(any(Recipe.class));

        Recipe result = serviceSpy.edit(recipeDto);

        //then
        assertNotNull(result);
        assertEquals(recipe,result);
        verify(serviceSpy).findById(anyLong());
        verify(noteService).findById(anyLong());
        verify(noteService).save(any(Note.class));
        verify(ingredientService,times(2)).findById(anyLong());
        verify(ingredientService,times(2)).save(any(Ingredient.class));
        verify(categoryService,times(2)).findById(anyLong());
        verify(categoryService,times(2)).save(any(Category.class));
        verify(serviceSpy).save(any(Recipe.class));
    }
}