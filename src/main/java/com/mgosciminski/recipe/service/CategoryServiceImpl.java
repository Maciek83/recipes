package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.converter.CategoryDtoToCategory;
import com.mgosciminski.recipe.domain.Category;
import com.mgosciminski.recipe.model.CategoryDto;
import com.mgosciminski.recipe.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryDtoToCategory categoryDtoToCategory;

    private Category nullObject = new Category();
    private final String BAD = "bad";

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryDtoToCategory categoryDtoToCategory) {
        this.categoryRepository = categoryRepository;
        this.categoryDtoToCategory = categoryDtoToCategory;
    }

    @Override
    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {

        Optional<Category> optionalCategory = categoryRepository.findById(id);

        if(optionalCategory.isPresent())
        {
            return optionalCategory.get();
        }
        else
        {
            nullObject.setDepartmentName(BAD);
            return nullObject;
        }
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category save(CategoryDto categoryDto) {
        return categoryRepository.save(categoryDtoToCategory.convert(categoryDto));
    }

    @Override
    public Category edit(CategoryDto categoryDto) {
        Category fromDataBase = findById(categoryDto.getId());
        fromDataBase.setDepartmentName(categoryDto.getDepartmentName());
        return save(fromDataBase);
    }

    @Override
    public void delete(Category category) {
        categoryRepository.delete(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
