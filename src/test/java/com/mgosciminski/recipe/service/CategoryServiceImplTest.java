package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.Category;
import com.mgosciminski.recipe.repository.CategoryRepository;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class CategoryServiceImplTest {

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryServiceImpl categoryService;

    CategoryServiceImpl categoryServiceSpy;

    Set<Category> categorySet = new HashSet<>();

    @Before
    public void setup()
    {
        categorySet.add(new Category());

        categoryServiceSpy = Mockito.spy(categoryService);
    }

    @Test
    public void findAll() {

        //given
        when(categoryRepository.findAll()).thenReturn(categorySet);

        //when
        Set<Category> result = (Set<Category>) categoryService.findAll();

        //then
        assertNotNull(result);
        assertEquals(result,categorySet);
        assertEquals(result.size(),1);
        verify(categoryRepository).findAll();
    }

    @Test
    public void findById() {

        //given
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(new Category()));

        //when
        Optional<Category> result = categoryService.findById(anyLong());

        //then
        assertNotNull(result);
        verify(categoryRepository).findById(anyLong());
    }

    @Test
    public void savePresent() {

        //given
        Category category = new Category();
        category.setName("bobo");
        Optional<Category> optionalCategory = Optional.of(category);
        doReturn(optionalCategory).when(categoryServiceSpy).findByName(anyString());

        //when
        Category result = categoryServiceSpy.save(category);

        //then
        assertNotNull(result);
        assertEquals(result,category);
        verifyZeroInteractions(categoryRepository);
    }

    @Test
    public void saveNotPresent()
    {
        //given
        Category category = new Category();
        category.setName("bobo");
        doReturn(Optional.empty()).when(categoryServiceSpy).findByName(anyString());
        when(categoryRepository.save(any())).thenReturn(category);

        //when
        Category result = categoryServiceSpy.save(category);

        //then
        assertNotNull(result);
        assertEquals(result,category);
        verify(categoryServiceSpy).findByName(anyString());
        verify(categoryRepository).save(any());
    }

    @Test
    public void delete() {

        //when
        categoryService.delete(any());

        //then
        verify(categoryRepository).delete(any());
    }

    @Test
    public void deleteById() {

        //when
        categoryService.deleteById(anyLong());

        //then
        verify(categoryRepository).deleteById(anyLong());
    }

    @Test
    public void findByName() {

        //given
        Optional<Category> optionalCategory = Optional.of(new Category());
        when(categoryRepository.findByName(anyString())).thenReturn(optionalCategory);

        //when
        Optional<Category> result = categoryService.findByName(anyString());

        //then
        assertNotNull(result);
        assertEquals(result,optionalCategory);
        verify(categoryRepository).findByName(anyString());
    }
}