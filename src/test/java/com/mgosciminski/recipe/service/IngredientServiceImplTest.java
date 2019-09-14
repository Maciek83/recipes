package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.converter.IngredientDtoToIngredient;
import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.model.IngredientDto;
import com.mgosciminski.recipe.repository.IngredientRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class IngredientServiceImplTest {

    @Mock
    IngredientRepository repository;
    @Mock
    IngredientDtoToIngredient converter;
    @Mock
    UomService uomService;

    @InjectMocks
    IngredientServiceImpl service;

    private Set<Ingredient> ingredients = new HashSet<>();

    private final String OK = "ok";
    private final String BAD = "bad";

    @Before
    public void setUp() throws Exception {

        ingredients.add(new Ingredient());
        ingredients.add(new Ingredient());
    }

    @Test
    public void findAll() throws Exception {

        //when
        when(repository.findAll()).thenReturn(ingredients);
        Iterable<Ingredient> result = service.findAll();


        //then
        assertNotNull(result);
        assertEquals(result,ingredients);

        verify(repository,times(1)).findAll();
    }

    @Test
    public void saveIngredient() throws Exception
    {
        //given
        Ingredient ingredient = new Ingredient();

        //when
        when(repository.save(any())).thenReturn(ingredient);
        Ingredient ingredientSaved = service.save(new Ingredient());

        //then
        assertNotNull(ingredientSaved);
        assertEquals(ingredient,ingredientSaved);

        verify(repository).save(any());
    }

    @Test
    public void saveIngredientDto() throws Exception {

        //given
        Ingredient ingredient = new Ingredient();

        //when
        when(repository.save(any())).thenReturn(ingredient);
        Ingredient ingredientSaved = service.save(new IngredientDto());

        //then
        assertNotNull(ingredientSaved);
        assertEquals(ingredient,ingredientSaved);

        verify(repository,times(1)).save(any());
    }

    @Test
    public void findById() throws Exception {
        //given
        Ingredient ingredient = new Ingredient();
        ingredient.setDescription(OK);
        Optional<Ingredient> ingredientOptional = Optional.of(ingredient);

        //when
        when(repository.findById(anyLong())).thenReturn(ingredientOptional);
        Ingredient result = service.findById(1L);

        //then
        assertNotNull(result);
        assertEquals(result.getDescription(),OK);

        verify(repository,times(1)).findById(anyLong());
    }

    @Test
    public void findByIdNull() throws Exception
    {
        //given
        Optional<Ingredient> optionalIngredient = Optional.empty();

        //when
        when(repository.findById(anyLong())).thenReturn(optionalIngredient);
        Ingredient result = service.findById(1L);

        //then
        assertNotNull(result);
        assertEquals(result.getDescription(),BAD);

        verify(repository,times(1)).findById(anyLong());
    }

    @Test
    public void deleteById() throws Exception {

        service.deleteById(1L);

        verify(repository,times(1)).deleteById(anyLong());

    }

    @Test
    public void delete() throws Exception{

        service.delete(new Ingredient());

        verify(repository,times(1)).delete(any());
    }
}