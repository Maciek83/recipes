package com.mgosciminski.recipe.converter;

import com.mgosciminski.recipe.domain.Category;
import com.mgosciminski.recipe.model.CategoryDto;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CategoryDtoToCategory implements Converter<CategoryDto, Category> {

    @Nullable
    @Override
    public Category convert(CategoryDto categoryDto) {

        if(categoryDto == null)
        {
            return null;
        }

        Category category = new Category();
        category.setDepartmentName(categoryDto.getDepartmentName());

        return category;
    }
}
