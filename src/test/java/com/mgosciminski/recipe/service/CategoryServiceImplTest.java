package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.converter.CategoryDtoToCategory;
import com.mgosciminski.recipe.domain.Category;
import com.mgosciminski.recipe.model.CategoryDto;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class CategoryServiceImplTest {

    @Mock
    CategoryRepository categoryRepository;
    @Mock
    CategoryDtoToCategory categoryDtoToCategory;

    @InjectMocks
    CategoryServiceImpl categoryService;

    private CategoryServiceImpl categoryServiceSpy;

    private Set<Category> categories = new HashSet<>();
    private final String BAD = "bad";

    @Before
    public void setup()
    {
        categories.add(new Category());
        categories.add(new Category());

        categoryServiceSpy = Mockito.spy(categoryService);
    }

    @Test
    public void findAll() {

        //when
        when(categoryRepository.findAll()).thenReturn(categories);
        Set<Category> result = (Set<Category>) categoryService.findAll();
        //then
        assertNotNull(result);
        assertEquals(result.size(),2);
        verify(categoryRepository).findAll();
    }

    @Test
    public void findByIdNull()
    {
        //given
        Optional<Category> optionalCategory = Optional.empty();

        //when
        when(categoryRepository.findById(anyLong())).thenReturn(optionalCategory);
        Category result = categoryService.findById(1L);

        //then
        assertNotNull(result);
        assertEquals(result.getName(),BAD);

        verify(categoryRepository).findById(anyLong());
    }

    @Test
    public void findById() {

        //given
        Optional<Category> optionalCategory = Optional.of(new Category());

        //when
        when(categoryRepository.findById(anyLong())).thenReturn(optionalCategory);
        Category result = categoryService.findById(1L);

        //then
        assertNotNull(result);
        assertEquals(optionalCategory.get(),result);

        verify(categoryRepository).findById(anyLong());
    }

    @Test
    public void saveCategoryDto()
    {
        //given
        Category category = new Category();

        //when
        when(categoryRepository.save(any())).thenReturn(category);
        Category result = categoryService.save(new CategoryDto());

        //then
        assertNotNull(result);
        assertEquals(category,result);

        verify(categoryRepository).save(any());
    }

    @Test
    public void saveCategory()
    {
        //given
        Category category = new Category();

        //when
        when(categoryRepository.save(any())).thenReturn(category);
        Category result = categoryService.save(new Category());

        //then
        assertNotNull(result);
        assertEquals(category,result);

        verify(categoryRepository).save(any());
    }

    @Test
    public void edit()
    {
        //given
        Category category = new Category();
        category.setName("name");

        //when
        doReturn(category).when(categoryServiceSpy).findById(anyLong());
        Category result = categoryServiceSpy.findById(1L);
        result.setName("newName");
        doReturn(result).when(categoryServiceSpy).save(any(Category.class));
        Category savedInDataBase = categoryServiceSpy.save(result);

        //then
        assertNotNull(savedInDataBase);
        assertEquals(category,result);
        assertEquals(result,savedInDataBase);
        assertEquals(savedInDataBase.getName(),"newName");

        verify(categoryServiceSpy).findById(anyLong());
        verify(categoryServiceSpy).save(any(Category.class));
    }

    @Test
    public void delete() {
        categoryService.delete(new Category());

        verify(categoryRepository).delete(any());
    }

    @Test
    public void deleteById() {
        categoryService.deleteById(1L);

        verify(categoryRepository).deleteById(anyLong());
    }
}