package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.Ingredient;
import com.mgosciminski.recipe.repository.IngredientRepository;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class IngredientServiceImplTest {

    @Mock
    IngredientRepository ingredientRepository;

    @InjectMocks
    IngredientServiceImpl ingredientService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final String NOT_FOUND = "can't find this id";
    private Set<Ingredient> ingredientSet = new HashSet<>();

    @Before
    public void setup()
    {
        ingredientSet.add(new Ingredient());
    }

    @Test
    public void findAll() {
        //given
        when(ingredientRepository.findAll()).thenReturn(ingredientSet);

        //then
        Set<Ingredient> result = (Set<Ingredient>) ingredientService.findAll();

        //then
        assertNotNull(result);
        assertEquals(ingredientSet,result);
        assertEquals(result.size(),1);
        verify(ingredientRepository).findAll();
    }

    @Test
    public void save() {
        //given
        Ingredient ingredient = new Ingredient();
        when(ingredientRepository.save(any())).thenReturn(ingredient);

        //when
        Ingredient result = ingredientService.save(any());

        //then
        assertNotNull(result);
        assertEquals(ingredient,result);
        verify(ingredientRepository).save(any());
    }

    @Test
    public void findByIdPresent() throws NotFoundException {

        //given
        when(ingredientRepository.findById(anyLong())).thenReturn(Optional.of(new Ingredient()));

        //when
        Ingredient result = ingredientService.findById(anyLong());

        //then
        assertNotNull(result);
        verify(ingredientRepository).findById(anyLong());
    }

    @Test(expected = NotFoundException.class)
    public void findByIdNotPresent() throws NotFoundException {
        //given
        when(ingredientRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Ingredient result = ingredientService.findById(anyLong());

        //then
        verify(ingredientRepository).findById(anyLong());
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(NOT_FOUND);
    }

    @Test
    public void deleteById() {

        //when
        ingredientService.deleteById(anyLong());

        //then
        verify(ingredientRepository).deleteById(anyLong());
    }

    @Test
    public void delete() {

        //when
        ingredientService.delete(any());

        //then
        verify(ingredientRepository).delete(any());
    }
}