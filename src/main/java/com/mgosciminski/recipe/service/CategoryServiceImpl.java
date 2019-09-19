package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.converter.CategoryDtoToCategory;
import com.mgosciminski.recipe.converter.CategoryToCategoryDto;
import com.mgosciminski.recipe.domain.Category;
import com.mgosciminski.recipe.model.CategoryDto;
import com.mgosciminski.recipe.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryDtoToCategory categoryDtoToCategory;
    private final CategoryToCategoryDto categoryToCategoryDto;

    private Category nullObject = new Category();
    private final String BAD = "bad";

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryDtoToCategory categoryDtoToCategory, CategoryToCategoryDto categoryToCategoryDto) {
        this.categoryRepository = categoryRepository;
        this.categoryDtoToCategory = categoryDtoToCategory;
        this.categoryToCategoryDto = categoryToCategoryDto;
    }

    @Override
    public Iterable<CategoryDto> findAll() {
        List<CategoryDto> categoriesDto = new LinkedList<>();
        categoryRepository.findAll().forEach(category -> categoriesDto.add(convertCategoryToCategoryDto(category)));
        return categoriesDto;
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
    public Category save(CategoryDto categoryDto) {

        Optional<Category> optionalCategory = findByName(categoryDto.getName());

        return optionalCategory.orElseGet(() -> categoryRepository.save(categoryDtoToCategory.convert(categoryDto))) ;
    }

    @Override
    public Category edit(CategoryDto categoryDto) {

        Optional<Category> optionalCategory = findById(categoryDto.getId());

        if(optionalCategory.isPresent())
        {
            Category category = optionalCategory.get();
            category.setName(categoryDto.getName());
            return save(category);
        }
        else
        {
            nullObject.setName("bad");
            nullObject.setId(-1L);

            return nullObject;
        }

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

    @Override
    public CategoryDto convertCategoryToCategoryDto(Category category) {
        return categoryToCategoryDto.convert(category);
    }
}