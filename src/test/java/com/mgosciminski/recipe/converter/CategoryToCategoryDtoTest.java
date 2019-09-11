package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.Category;
import com.mgosciminski.recipe.model.CategoryDto;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryToCategoryDtoTest {

    CategoryToCategoryDto converter;

    @Before
    public void setUp()
    {
        converter = new CategoryToCategoryDto();
    }

    @Test
    public void convertNull() throws Exception
    {
        CategoryDto categoryDtoNull = converter.convert(null);

        assertNull(categoryDtoNull);
    }

    @Test
    public void convert() throws Exception
    {
        //given
        Long id = 1L;
        Category category = new Category();
        category.setId(id);
        category.setName("name");

        //when
        CategoryDto categoryDto = converter.convert(category);

        //then
        assertNotNull(categoryDto);
        assertEquals(categoryDto.getId(),id);
        assertEquals(categoryDto.getName(),"name");

    }
}