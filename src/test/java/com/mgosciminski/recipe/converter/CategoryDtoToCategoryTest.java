package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.Category;
import com.mgosciminski.recipe.model.CategoryDto;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryDtoToCategoryTest {

    CategoryDtoToCategory converter;

    @Before
    public void setUp() throws Exception {
        converter = new CategoryDtoToCategory();
    }

    @Test
    public void convertNull()
    {
        Category nullCategory = converter.convert(null);

        assertNull(nullCategory);
    }

    @Test
    public void convert() {
        //given
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setDepartmentName("department");

        //when
        Category category = converter.convert(categoryDto);

        //then
        assertNotNull(category);
        assertEquals(category.getDepartmentName(),"department");

    }
}