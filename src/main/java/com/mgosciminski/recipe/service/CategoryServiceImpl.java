package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.Category;
import com.mgosciminski.recipe.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Iterable<Category> findAll() {

        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {

        return categoryRepository.findById(id);
    }

    @Override
    public Category save(Category category) {

        Optional<Category> optionalCategory = findByName(category.getName());

        return optionalCategory.orElseGet(() -> categoryRepository.save(category));
    }

    @Override
    public void delete(Category category) {
        categoryRepository.delete(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }


}