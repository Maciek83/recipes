package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.Category;
import com.mgosciminski.recipe.model.CategoryDto;

public interface CategoryService {
    Iterable<Category> findAll();
    Category findById(Long id);
    Category save(Category category);
    Category save(CategoryDto categoryDto);
    Category edit(CategoryDto categoryDto);
    void delete(Category category);
    void deleteById(Long id);
}
