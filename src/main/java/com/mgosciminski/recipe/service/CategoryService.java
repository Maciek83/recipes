package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.Category;
import com.mgosciminski.recipe.model.CategoryDto;
import javassist.NotFoundException;

import java.util.Optional;

public interface CategoryService {
    Iterable<CategoryDto> findAllDto();
    Optional<Category> findById(Long id);
    Category findByIdPresentOfException(Long id) throws NotFoundException;
    CategoryDto findDtoById(Long id) throws NotFoundException;
    Category save(Category category);
    Category save(CategoryDto categoryDto);
    Category edit(CategoryDto categoryDto);
    void delete(Category category);
    void deleteById(Long id);
    Optional<Category> findByName(String name);
    CategoryDto convertCategoryToCategoryDto(Category category);
}
