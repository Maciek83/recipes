package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.*;
import com.mgosciminski.recipe.repository.RecipeRepository;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;


@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository repository;
    private final String NOT_FOUND = "can't find this id";


    public RecipeServiceImpl(RecipeRepository repository) {
        this.repository = repository;

    }

    @Override
    public Recipe save(Recipe recipe) {

        return repository.save(recipe);
    }

    @Override
    public Iterable<Recipe> findAll() {
        return repository.findAll();
    }

    @Override
    public Recipe findById(Long id) throws NotFoundException
    {
        return repository
                .findById(id)
                .orElseThrow(()->new NotFoundException(NOT_FOUND));
    }

    @Override
    public void delete(Recipe recipe) {

        repository.delete(recipe);
    }

    @Override
    public void deleteById(Long id) {

        repository.deleteById(id);
    }

    @Override
    public Recipe edit(Recipe recipe) throws NotFoundException {

        Recipe recipeFromDb = findById(recipe.getId());
        recipeFromDb.setDescription(recipe.getDescription());
        recipeFromDb.setPrepTime(recipe.getPrepTime());
        recipeFromDb.setCookTime(recipe.getCookTime());
        recipeFromDb.setServings(recipe.getServings());
        recipeFromDb.setSource(recipe.getSource());
        recipeFromDb.setUrl(recipe.getUrl());
        recipeFromDb.setDirections(recipe.getDirections());
        recipeFromDb.setDifficulty(recipe.getDifficulty());

        return save(recipeFromDb);
    }


}
