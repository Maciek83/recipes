package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.Recipe;
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
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class RecipeServiceImplTest {

    @Mock
    RecipeRepository repository;
    @InjectMocks
    RecipeServiceImpl recipeService;

    RecipeServiceImpl recipeServiceSpy;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final String NOT_FOUND = "can't find this id";
    private Set<Recipe> recipes = new HashSet<>();

    @Before
    public void setup()
    {
        recipes.add(new Recipe());

        recipeServiceSpy = Mockito.spy(recipeService);
    }

    @Test
    public void save() {

        //given
        Recipe recipe = new Recipe();
        when(repository.save(any())).thenReturn(recipe);

        //when
        Recipe result = recipeService.save(new Recipe());

        //then
        assertNotNull(result);
        assertEquals(recipe,result);
        verify(repository,times(1)).save(any());
    }

    @Test
    public void findAll() {

        //given
        when(repository.findAll()).thenReturn(recipes);

        //when
        Set<Recipe> result = (Set<Recipe>) recipeService.findAll();

        //then
        assertNotNull(result);
        assertEquals(recipes,result);
        assertEquals(1,result.size());
        verify(repository).findAll();

    }

    @Test
    public void findByIdExist() throws NotFoundException {
        //given
        when(repository.findById(anyLong())).thenReturn(Optional.of(new Recipe()));

        //when
        Recipe result = recipeService.findById(1L);

        //then
        assertNotNull(result);
        verify(repository).findById(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void findByIdException() throws NotFoundException {
        //given
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Recipe result = recipeService.findById(1L);

        //then
        verify(repository).findById(anyLong());
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(NOT_FOUND);

    }

    @Test
    public void delete() {

        //when
        recipeService.delete(new Recipe());

        //then
        verify(repository).delete(any());
    }

    @Test
    public void deleteById() {

        //when
        recipeService.deleteById(anyLong());

        //then
        verify(repository).deleteById(anyLong());
    }

    @Test
    public void editExisting() throws NotFoundException {

        //given
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        doReturn(recipe).when(recipeServiceSpy).findById(anyLong());
        doReturn(recipe).when(recipeServiceSpy).save(any());

        //when
        Recipe result = recipeServiceSpy.edit(recipe);

        //then
        assertNotNull(result);
        assertEquals(recipe,result);
        verify(recipeServiceSpy).findById(anyLong());
        verify(recipeServiceSpy).save(any());
    }

    @Test(expected = NotFoundException.class)
    public void editThrowException() throws NotFoundException {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        doThrow(new NotFoundException(NOT_FOUND)).when(recipeServiceSpy).findById(anyLong());

        //when
        Recipe result = recipeServiceSpy.edit(recipe);

        //then
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(NOT_FOUND);
    }
}