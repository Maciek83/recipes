package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.Category;

import java.util.Optional;

public interface CategoryService {
    Iterable<Category> findAll();
    Optional<Category> findById(Long id);
    Category save(Category category);
    void delete(Category category);
    void deleteById(Long id);
    Optional<Category> findByName(String name);

}
