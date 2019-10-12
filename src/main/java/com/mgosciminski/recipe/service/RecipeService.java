package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.Recipe;
import javassist.NotFoundException;



public interface RecipeService {

    Recipe save(Recipe recipe);
    Iterable<Recipe> findAll();
    Recipe findById(Long id) throws NotFoundException;
    void delete(Recipe recipe);
    void deleteById(Long id);
    Recipe edit(Recipe recipe) throws NotFoundException;
}
