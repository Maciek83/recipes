package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.converter.CategoryDtoToCategory;
import com.mgosciminski.recipe.converter.CategoryToCategoryDto;
import com.mgosciminski.recipe.domain.Category;
import com.mgosciminski.recipe.model.CategoryDto;
import com.mgosciminski.recipe.repository.CategoryRepository;
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
import java.util.List;
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
    @Mock
    CategoryToCategoryDto categoryToCategoryDto;

    @InjectMocks
    CategoryServiceImpl categoryService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CategoryServiceImpl categoryServiceSpy;

    private Set<Category> categories = new HashSet<>();
    private final String NOT_FOUND = "can't find this id";

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
        //given
        when(categoryRepository.findAll()).thenReturn(categories);
        doReturn(new CategoryDto()).when(categoryServiceSpy).convertCategoryToCategoryDto(any(Category.class));

        //when
        List<CategoryDto> result = (List<CategoryDto>) categoryServiceSpy.findAllDto();

        //then
        assertNotNull(result);
        assertEquals(result.size(),2);
        verify(categoryServiceSpy,times(2)).convertCategoryToCategoryDto(any(Category.class));
        verify(categoryRepository).findAll();
    }

    @Test
    public void findById() throws Exception
    {

        //given
        Optional<Category> optionalCategory = Optional.of(new Category());

        //when
        when(categoryRepository.findById(anyLong())).thenReturn(optionalCategory);
        Optional<Category> result = categoryService.findById(1L);

        //then
        assertNotNull(result);
        assertEquals(optionalCategory.get(),result.get());

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
    public void editPresent() throws Exception
    {
        //given
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        Category category = new Category();
        category.setId(1L);
        category.setName("cat");
        doReturn(Optional.of(category)).when(categoryServiceSpy).findById(anyLong());
        doReturn(category).when(categoryServiceSpy).save(any(Category.class));

        //when
        Category result = categoryServiceSpy.edit(categoryDto);

        //then
        assertNotNull(result);
        verify(categoryServiceSpy).findById(anyLong());
        verify(categoryServiceSpy).save(any(Category.class));

    }

    @Test
    public void editNotPresent() throws Exception
    {
        //given
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        doReturn(Optional.empty()).when(categoryServiceSpy).findById(anyLong());

        //when
        Category result = categoryServiceSpy.edit(categoryDto);

        //then
        assertNotNull(result);
        assertEquals(result.getName(),"bad");
        verify(categoryServiceSpy).findById(anyLong());
        verify(categoryServiceSpy,times(0)).save(any(Category.class));
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

    @Test
    public void  convertCategoryToCategoryDtoTest() throws Exception
    {
        //given
        when(categoryToCategoryDto.convert(any(Category.class))).thenReturn(any(CategoryDto.class));

        //when
        CategoryDto result = categoryService.convertCategoryToCategoryDto(new Category());

        //then
        verify(categoryToCategoryDto).convert(any(Category.class));

    }

    @Test
    public void findDtoByIdExist() throws Exception
    {
        //given
        doReturn(Optional.of(new Category())).when(categoryServiceSpy).findById(anyLong());
        doReturn(new CategoryDto()).when(categoryServiceSpy).convertCategoryToCategoryDto(any());
        //when

        categoryServiceSpy.findDtoById(1L);

        //then
        verify(categoryServiceSpy).findById(anyLong());
        verify(categoryServiceSpy).convertCategoryToCategoryDto(any());
    }

    @Test(expected = NotFoundException.class)
    public void findDtoByIdNotExist()throws Exception
    {
        //given
        doReturn(Optional.empty()).when(categoryServiceSpy).findById(anyLong());

        //when
        categoryServiceSpy.findDtoById(1L);

        //then
        verify(categoryServiceSpy).findDtoById(anyLong());
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(NOT_FOUND);
    }

    @Test
    public void findByIdPresentOfException_present() throws Exception
    {
        //given
        doReturn(Optional.of(new Category())).when(categoryServiceSpy).findById(anyLong());

        //when
        categoryServiceSpy.findById(1L);

        //then
        verify(categoryServiceSpy).findById(anyLong());

    }

    @Test(expected = NotFoundException.class)
    public void findByIdPresentOfException_notPresent()throws Exception
    {
        //given
        doReturn(Optional.empty()).when(categoryServiceSpy).findById(anyLong());

        //when
        categoryServiceSpy.findByIdPresentOfException(1L);

        //then
        verify(categoryServiceSpy).findById(anyLong());
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(NOT_FOUND);
    }
}