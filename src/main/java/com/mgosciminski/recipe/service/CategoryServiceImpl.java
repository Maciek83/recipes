package com.mgosciminski.recipe.service;


import com.mgosciminski.recipe.domain.Category;
import com.mgosciminski.recipe.repository.CategoryRepository;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final String NOT_FOUND = "can't find this id";

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
    public Category findByIdPresentOfException(Long id) throws NotFoundException {

        Optional<Category> optionalCategory = findById(id);

        return optionalCategory.orElseThrow(()-> new NotFoundException(NOT_FOUND));
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