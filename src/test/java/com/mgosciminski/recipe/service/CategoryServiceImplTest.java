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
        Category category = new Category();
        category.setName("name");
        categories.add(category);
        categories.add(new Category());

        categoryServiceSpy = Mockito.spy(categoryService);
    }

    @Test
    public void findAll() throws Exception
    {

        //when
        when(categoryRepository.findAll()).thenReturn(categories);
        Set<Category> result = (Set<Category>) categoryService.findAll();
        //then
        assertNotNull(result);
        assertEquals(result.size(),2);
        verify(categoryRepository).findAll();
    }

    @Test
    public void findByIdNull() throws Exception
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
    public void findById() throws Exception
    {

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
    public void saveCategoryDtoIsInDatabase() throws Exception
    {
        //given
        Category category = new Category();
        category.setName("name");
        Optional<Category> categoryOptional = Optional.of(category) ;
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("x");

        //when
        doReturn(categoryOptional).when(categoryServiceSpy).findByName(anyString());
        Category result = categoryServiceSpy.save(categoryDto);

        //then
        assertNotNull(result);
        assertEquals(result.getName(),category.getName());
        verify(categoryServiceSpy).findByName(anyString());
        verifyZeroInteractions(categoryRepository);
    }

    @Test
    public void saveCategoryDtoNoExistInDB() throws Exception
    {
        //given
        Category category = new Category();
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("xx");
        Optional<Category> optionalCategory = Optional.empty();

        //when
        doReturn(optionalCategory).when(categoryServiceSpy).findByName(anyString());
        when(categoryRepository.save(any())).thenReturn(category);
        Category result = categoryServiceSpy.save(categoryDto);

        //then
        assertNotNull(result);
        assertEquals(category,result);

        verify(categoryServiceSpy).findByName(anyString());
        verify(categoryRepository).save(any());
    }

    @Test
    public void saveCategory() throws Exception
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
    public void edit() throws Exception
    {
        //given
        Category category = new Category();
        category.setName("name");

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("newName");

        //when
        doReturn(category).when(categoryServiceSpy).findById(anyLong());
        doReturn(category).when(categoryServiceSpy).save(any(Category.class));
        Category savedInDataBase = categoryServiceSpy.edit(categoryDto);


        //then
        assertNotNull(savedInDataBase);
        assertEquals(category,savedInDataBase);

        verify(categoryServiceSpy).findById(anyLong());
        verify(categoryServiceSpy).save(any(Category.class));
    }

    @Test
    public void delete() throws Exception
    {
        categoryService.delete(new Category());

        verify(categoryRepository).delete(any());
    }

    @Test
    public void deleteById() throws Exception
    {
        categoryService.deleteById(1L);

        verify(categoryRepository).deleteById(anyLong());
    }

    @Test
    public void findByName() throws Exception
    {
        //given
        Optional<Category> categoryOptional = Optional.of(new Category());

        //when
        when(categoryRepository.findByName(anyString())).thenReturn(categoryOptional);
        Optional<Category> result = categoryService.findByName("x");

        //then
        assertNotEquals(result, Optional.empty());
        verify(categoryRepository).findByName(anyString());

    }

    @Test
    public void findByNameNull() throws Exception
    {
        //given
        Optional<Category> optionalCategory = Optional.empty();

        //when
        when(categoryRepository.findByName(anyString())).thenReturn(optionalCategory);
        Optional<Category> result = categoryService.findByName("x");

        //then
        assertEquals(result,Optional.empty());
        verify(categoryRepository).findByName(anyString());
    }
}