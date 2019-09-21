package com.mgosciminski.recipe.service;

import com.mgosciminski.recipe.domain.Recipe;
import com.mgosciminski.recipe.model.RecipeDto;

public interface RecipeService {

    Recipe save(RecipeDto recipeDto);
    Recipe save(Recipe recipe);
    Iterable<Recipe> findAll();
    Iterable<RecipeDto> findAllDto();
    Recipe findById(Long id);
    void delete(Recipe recipe);
    void deleteById(Long id);
    Recipe edit(RecipeDto recipeDto);
    RecipeDto convertRecipeToRecipeDto(Recipe recipe);

}
