package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.converter.*;
import com.mgosciminski.recipe.domain.*;
import com.mgosciminski.recipe.model.CategoryDto;
import com.mgosciminski.recipe.model.IngredientDto;
import com.mgosciminski.recipe.model.NoteDto;
import com.mgosciminski.recipe.model.RecipeDto;
import com.mgosciminski.recipe.repository.RecipeRepository;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.LinkedList;
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
    @Mock
    RecipeToRecipeDto recipeToRecipeDto;


    @InjectMocks
    RecipeServiceImpl service;

    private RecipeServiceImpl serviceSpy;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Set<Recipe> recipes;

    private final String NOT_FOUND = "can't find this id";


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
    public void findAllDto()
    {
        //given
        LinkedList<Recipe> recipes = new LinkedList<>();
        recipes.add(new Recipe());
        recipes.add(new Recipe());
        doReturn(new RecipeDto()).when(serviceSpy).convertRecipeToRecipeDto(any());
        doReturn(recipes).when(serviceSpy).findAll();
        //when
        LinkedList<RecipeDto> recipeDtos = (LinkedList<RecipeDto>) serviceSpy.findAllDto();

        //then
        assertNotNull(recipeDtos);
        assertEquals(recipeDtos.size(),2);
        verify(serviceSpy,times(2)).convertRecipeToRecipeDto(any());
        verify(serviceSpy).findAll();

    }

    @Test(expected = NotFoundException.class)
    public void findByIdNull() throws NotFoundException {

        //when
        service.findById(1L);
        when(repository.findById(anyLong())).thenThrow(new NotFoundException(NOT_FOUND));

        //then
        verify(repository).findById(anyLong());
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(NOT_FOUND);
    }

    @Test
    public void findById() throws NotFoundException {
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
    public void edit() throws NotFoundException {

        //given
        Recipe recipe = new Recipe();
        recipe.setId(1L);


        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(1L);
        recipeDto.setDifficulty("EASY");
        NoteDto noteDto = new NoteDto();
        noteDto.setId(1L);
        recipeDto.setNoteDto(noteDto);



        //when
        doReturn(recipe).when(serviceSpy).findById(anyLong());
        when(noteService.findById(anyLong())).thenReturn(new Note());
        when(noteService.save(any(Note.class))).thenReturn(new Note());
        doReturn(recipe).when(serviceSpy).save(any(Recipe.class));

        Recipe result = serviceSpy.edit(recipeDto);

        //then
        assertNotNull(result);
        assertEquals(recipe,result);
        verify(serviceSpy).findById(anyLong());
        verify(noteService).findById(anyLong());
        verify(noteService).save(any(Note.class));
        verify(serviceSpy).save(any(Recipe.class));
    }

    @Test
    public void convertRecipeToRecipeDto()
    {
        //given
        when(recipeToRecipeDto.convert(any(Recipe.class))).thenReturn(new RecipeDto());

        //when
        RecipeDto recipeDto = service.convertRecipeToRecipeDto(new Recipe());

        //then
        verify(recipeToRecipeDto).convert(any(Recipe.class));
    }

    @Test
    public void findDtoByIdOk() throws Exception
    {
        //given
        RecipeDto recipeDto = new RecipeDto();
        doReturn(new Recipe()).when(serviceSpy).findById(anyLong());
        doReturn(recipeDto).when(serviceSpy).convertRecipeToRecipeDto(any());

        //when
        RecipeDto result = serviceSpy.findDtoById(1L);

        //then
        assertNotNull(result);
        assertEquals(recipeDto,result);
        verify(serviceSpy).convertRecipeToRecipeDto(any());
        verify(serviceSpy).findById(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void findDtoByIdException() throws Exception
    {
        doThrow( new NotFoundException(NOT_FOUND)).when(serviceSpy).findById(anyLong());

        serviceSpy.findDtoById(1L);

        thrown.expect(NotFoundException.class);
        thrown.expectMessage(NOT_FOUND);
    }
}